package com.lfantastico.web;

import com.lfantastico.domain.Country;
import com.lfantastico.domain.DbAccess;
import com.lfantastico.domain.Event;
import com.lfantastico.domain.Keyword;
import com.lfantastico.domain.Language;
import com.lfantastico.domain.MailTemplate;
import com.lfantastico.domain.PageVersion;
import com.lfantastico.domain.Payment;
import com.lfantastico.domain.Site;
import com.lfantastico.domain.SiteVersion;
import com.lfantastico.domain.State;
import com.lfantastico.domain.User;
import com.lfantastico.util.Util;
import org.tastefuljava.mvc.QuickMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Helper {
    private static final Logger LOG
            = LoggerFactory.getLogger(Helper.class);
    private static final String MAILTEMPLATE_NAMES[] = {
        "registration", "application", "confirmation", "rejection",
        "cancelation"
    };
    private static final ThreadLocal<Helper> HELPER
            = new ThreadLocal<Helper>();

    private DbAccess db;
    private Event event;
    private Site site;
    private String hostName;
    private User user;
    private String userName;

    public static Helper current() {
        return HELPER.get();
    }

    public static Helper create(String hostName, String userName) {
        Helper result = new Helper(hostName, userName);
        HELPER.set(result);
        return result;
    }

    private Helper(String hostName, String userName) {
        this.hostName = hostName;
        this.userName = userName;
        this.db = DbAccess.open();
    }

    public void commit() {
        db.commit();
    }

    public void close() {
        HELPER.remove();
        db.close();
    }

    public void save(Object obj) {
        db.save(obj);
    }

    public void update(Object obj) {
        db.update(obj);
    }

    public void saveOrUpdate(Object obj) {
        db.saveOrUpdate(obj);
    }

    public void merge(Object obj) {
        db.merge(obj);
    }

    public List<Language> getAllLanguages(String language) {
        List<Language> languages = new ArrayList<Language>();
        languages.addAll(db.getAll(Language.class));
        Collections.sort(languages, new LanguageLabelOrder(language));
        return languages;
    }

    public Language getLanguage(String code) {
        return db.get(Language.class, code);
    }

    public Map<String,Language> getLanguages() {
        return new QuickMap<String,Language>() {
            @Override
            public Language get(Object key) {
                return getLanguage((String)key);
            }
        };
    }

    public List<Country> getAllCountries(String language) {
        List<Country> countries = new ArrayList<Country>();
        countries.addAll(db.getAll(Country.class));
        Collections.sort(countries, new CountryLabelOrder(language));
        return countries;
    }

    public Map<String,Country> getCountries() {
        return new QuickMap<String,Country>() {
            @Override
            public Country get(Object key) {
                return db.get(Country.class, (String)key);
            }
        };
    }

    public List<State> getAllStates() {
        return State.getAll();
    }

    public Map<String,State> getStates() {
        return new QuickMap<String,State>() {
            @Override
            public State get(Object key) {
                return State.get((String)key);
            }
        };
    }

    public Site getSite() {
        if (site == null) {
            site = getSite(hostName);
            if (site == null) {
                site = getSite("localhost");
            }
        }
        return site;
    }

    public Site getSite(String hostName) {
        Criteria crit = db.createCriteria(Site.class);
        crit.add(Restrictions.eq("hostName", hostName));
        return (Site)crit.uniqueResult();
    }

    public Keyword getKeywordById(int id) {
        return db.get(Keyword.class, id);
    }

    public SiteVersion getSiteVersion(String language) {
        getSite();
        if (site == null) {
            return null;
        }
        SiteVersion result = site.getVersion(language);
        if (result == null) {
            result = site.getVersion("fr");
            if (result == null) {
                result = site.getVersion("en");
            }
        }
        return result;
    }

    public PageVersion getPageVersion(String name, String language) {
        SiteVersion siteVersion = getSiteVersion(language);
        return siteVersion.getPage(name);
    }

    public User getUser(String name) {
        if (name == null) {
            return null;
        }
        Query qry = db.createQuery("from User where email=:email");
        qry.setString("email", name);
        return (User)qry.uniqueResult();
    }

    public User getUserById(int id) {
        return db.get(User.class, id);
    }

    public User getUser() {
        if (userName == null) {
            return null;
        }
        if (user == null) {
            user = getUser(userName);
        }
        return user;
    }

    public Payment getPayment(String transactionId) {
        return db.get(Payment.class, transactionId);
    }

    public Event getEventById(int id) {
        return db.get(Event.class, id);
    }

    public Paginable<Event> getNextEvents() {
        return new PaginableCriteria<Event>() {
            @Override
            protected Criteria buildCriteria() {
                Criteria crit = db.createCriteria(Event.class);
                crit.add(Restrictions.gt("dateTime", new Date()));
                crit.add(Restrictions.eq("state", Event.State.SCHEDULED));
                crit.addOrder(Order.asc("dateTime"));
                return crit;
            }
        };
    }

    public List<Event> getRecentEvents() {
        Criteria crit = db.createCriteria(Event.class);
        crit.addOrder(Order.desc("creation"));
        crit.setMaxResults(20);
        @SuppressWarnings("unchecked")
        List<Event> result = crit.list();
        return result;
    }

    public Map<String,String> getSiteTexts(final String language,
            final boolean editMode) {
        return new QuickMap<String,String>() {
            @Override
            public String get(Object key) {
                StringBuilder buf = new StringBuilder();
                SiteVersion version = getSiteVersion(language);
                String s = version.getText((String)key);
                if (editMode) {
                    buf.append("<div class='editable'>");
                }
                buf.append("<div>");
                if (s != null) {
                    buf.append(s);
                } else {
                    buf.append("???");
                    buf.append(key);
                    buf.append("???");
                }
                buf.append("</div>");
                if (editMode) {
                    buf.append("<a href='editSiteText?key=");
                    buf.append(key);
                    buf.append("'>edit</a></div>");
                }
                return buf.toString();
            }
        };
    }

    public void setSiteText(String language, String key, String value) {
        SiteVersion version = getSiteVersion(language);
        version.setText(key, value);
    }

    public Map<String,String> getPageTexts(final String pageName,
            final String language, final boolean editMode) {
        return new QuickMap<String,String>() {
            @Override
            public String get(Object key) {
                StringBuilder buf = new StringBuilder();
                SiteVersion version = getSiteVersion(language);
                PageVersion pageVersion = version.getPage(pageName);
                String s = pageVersion == null
                        ? null : pageVersion.getText((String)key);
                if (editMode) {
                    buf.append("<div class='editable'>");
                }
                buf.append("<div>");
                if (s != null) {
                    buf.append(s);
                } else {
                    buf.append("???");
                    buf.append(key);
                    buf.append("???");
                }
                buf.append("</div>");
                if (editMode) {
                    buf.append("<a href='editPageText?key=");
                    buf.append(key);
                    buf.append("'>edit</a></div>");
                }
                return buf.toString();
            }
        };
    }

    public void setPageText(String pageName, String language, String key,
            String value) {
        SiteVersion version = getSiteVersion(language);
        PageVersion pageVersion = version.getPage(pageName);
        if (pageVersion == null) {
            pageVersion = new PageVersion();
            version.setPage(pageName, pageVersion);
        }
        pageVersion.setText(key, value);
    }

    public String[] getMailTemplateNames() {
        return MAILTEMPLATE_NAMES;
    }

    public MailTemplate getMailTemplate(String language, String name) {
        SiteVersion version = getSiteVersion(language);
        return version.getMailTemplate(name);
    }

    public Map<String,MailTemplate> getMailTemplates(String language) {
        SiteVersion version = getSiteVersion(language);
        return version.getMailTemplates();
    }

    public List<Event> getQueryEvents(Date dateMin, Date dateMax) {
        Criteria crit = db.createCriteria(Event.class);
        if (dateMin != null) {
            crit.add(Restrictions.ge("dateTime", dateMin));
        }
        if (dateMax != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateMax);
            cal.add(Calendar.DATE, 1);
            crit.add(Restrictions.lt("dateTime", cal.getTime()));
        }
        crit.addOrder(Order.asc("dateTime"));
        @SuppressWarnings("unchecked")
        List<Event> result = crit.list();
        return result;
    }

    public List<User> getQueryUsers(String nameFilter) {
        Criteria crit = db.createCriteria(User.class);
        if (!Util.isBlank(nameFilter)) {
            crit.add(Restrictions.ilike("lastName", nameFilter + "%"));
        }
        crit.addOrder(Order.desc("dateOfBirth"));
        @SuppressWarnings("unchecked")
        List<User> result = crit.list();
        return result;
    }

    public Map<Event,Map<String,String>> getEventSummaries() {
        return new QuickMap<Event,Map<String,String>>() {
            @Override
            public Map<String, String> get(Object key) {
                if (!(key instanceof Event)) {
                    return null;
                }
                final Event event = (Event)key;
                return new QuickMap<String,String>() {
                    @Override
                    public String get(Object key) {
                        if (!(key instanceof String)) {
                            return null;
                        }
                        String language = (String)key;
                        return getEventSummary(event, language);
                    }
                };
            }
        };
    }

    public Map<Event,Map<Integer,User>> getUserAtPlace() {
        return new QuickMap<Event,Map<Integer,User>>() {
            @Override
            public Map<Integer,User> get(Object key) {
                if (!(key instanceof Event)) {
                    return null;
                }
                final Event event = (Event)key;
                return new QuickMap<Integer,User>() {
                    @Override
                    public User get(Object key) {
                        if (!(key instanceof Number)) {
                            return null;
                        }
                        Number no = (Number)key;
                        return event.getUserAtPlace(no.intValue());
                    }
                };
            }
        };
    }

    private String getEventSummary(Event event, String language) {
        SiteVersion siteVersion = getSiteVersion(language);
        StringBuilder buf = new StringBuilder();
        buf.append(siteVersion.getString(
                "event.orientation." + event.getOrientation().getName()));
        if (event.getRelationship() != null) {
            buf.append(", ");
            buf.append(siteVersion.getString(
                    "event.relationship." + event.getRelationship().getName()));
        }
        for (Keyword kw: event.getKeywords()) {
            buf.append(", ");
            buf.append(kw.getText(language));
        }
        return buf.toString();
    }

    private static class LanguageLabelOrder implements Comparator<Language> {
        private String language;

        private LanguageLabelOrder(String language) {
            this.language = language;
        }

        public int compare(Language a, Language b) {
            String lbl1 = a.getLabel(language);
            if (lbl1 == null) {
                lbl1 = a.getDefaultLabel();
            }
            String lbl2 = b.getLabel(language);
            if (lbl2 == null) {
                lbl2 = b.getDefaultLabel();
            }
            return lbl1.compareToIgnoreCase(lbl2);
        }
    }

    private static class CountryLabelOrder implements Comparator<Country> {
        private String language;

        private CountryLabelOrder(String language) {
            this.language = language;
        }

        public int compare(Country a, Country b) {
            String lbl1 = a.getLabel(language);
            if (lbl1 == null) {
                lbl1 = a.getCode();
            }
            String lbl2 = b.getLabel(language);
            if (lbl2 == null) {
                lbl2 = b.getCode();
            }
            return lbl1.compareToIgnoreCase(lbl2);
        }
    }
}
