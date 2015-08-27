package com.lfantastico.web;

import com.lfantastico.domain.Country;
import com.lfantastico.domain.Language;
import com.lfantastico.domain.PageVersion;
import com.lfantastico.domain.SiteVersion;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import org.tastefuljava.mvc.Page;
import org.tastefuljava.mvc.Param;

public class BasePage extends Page {
    private Map<String,List<ErrorMessage>> errors
            = new HashMap<String,List<ErrorMessage>>();

    public void login() throws IOException {
        redirect();
    }

    public void setLanguage(@Param("language") String language)
            throws IOException {
        getSess().setLanguage(language);
        redirect();
    }

    public void setEditMode(@Param("on") boolean on) throws IOException {
        getSess().setEditMode(on);
        redirect();
    }

    public void setPageText(@Param("key") String key,
            @Param("text") String text) throws IOException {
        Helper helper = Helper.current();
        helper.setPageText(getName(), getLanguage(), key, text);
        helper.commit();
        redirect();
    }

    public void setSiteText(@Param("key") String key,
            @Param("text") String text) throws IOException {
        Helper helper = Helper.current();
        helper.setSiteText(getLanguage(), key, text);
        helper.commit();
        redirect();
    }

    public List<Language> getAllLanguages() {
        Helper helper = Helper.current();
        return helper.getAllLanguages(getLanguage());
    }

    public List<Country> getAllCountries() {
        Helper helper = Helper.current();
        return helper.getAllCountries(getLanguage());
    }

    public void addError(String name, String key, Object... args) {
        List<ErrorMessage> list = errors.get(key);
        if (list == null) {
            list = new ArrayList<ErrorMessage>();
            errors.put(name, list);
        }
        list.add(new ErrorMessage(key, args));
    }

    public Map<String,List<String>> getErrors() {
        Map<String,List<String>> result = new HashMap<String,List<String>>();
        for (Map.Entry<String,List<ErrorMessage>> entry: errors.entrySet()) {
            List<String> list = new ArrayList<String>();
            for (ErrorMessage em: entry.getValue()) {
                String pattern = getString(em.getKey());
                list.add(MessageFormat.format(pattern, em.getArgs()));
            }
            result.put(entry.getKey(), list);
        }
        errors.clear();
        return result;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public SessionHelper getSess() {
        return (SessionHelper)getSession().getAttribute("session");
    }

    public Locale getLocale() {
        return getSess().getLocale();
    }

    public String getLanguage() {
        return getSess().getLanguage();
    }

    public PageVersion getPageVersion() {
        Helper helper = Helper.current();
        return helper.getPageVersion(getName(),getLanguage());
    }

    public LocalizationContext getBundle() {
        ResourceBundle bundle = new SiteBundle();
        return new LocalizationContext(bundle, getLocale());
    }

    public String getString(String key) {
        Helper helper = Helper.current();
        SiteVersion version = helper.getSiteVersion(getLanguage());
        PageVersion page = version.getPage(getName());
        String s = page == null ? null : page.getString(key);
        if (s == null) {
            s = version.getString(key);
            if (s == null) {
                s = "???" + key + "???";
            }
        }
        return s;
    }

    public Map<String,String> getTexts() {
        Helper helper = Helper.current();
        return helper.getPageTexts(getName(), getLanguage(),
                getSess().getEditMode());
    }

    public Map<String,String> getSiteTexts() {
        Helper helper = Helper.current();
        return helper.getSiteTexts(getLanguage(), getSess().getEditMode());
    }

    private class SiteBundle extends ResourceBundle {
        @Override
        protected Object handleGetObject(String key) {
            return BasePage.this.getString(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            return new Enumeration<String>() {
                private Iterator<String> iter = getStringKeys().iterator();

                public boolean hasMoreElements() {
                    return iter.hasNext();
                }

                public String nextElement() {
                    return iter.next();
                }
            };
        }

        private Set<String> getStringKeys() {
            Helper helper = Helper.current();
            SiteVersion version = helper.getSiteVersion(getLanguage());
            Set<String> result = new HashSet<String>();
            result.addAll(version.getStrings().keySet());
            PageVersion page = version.getPage(getName());
            if (page != null) {
                result.addAll(page.getStrings().keySet());
            }
            return result;
        }
    }

    private static class ErrorMessage {
        private String key;
        private Object args[];

        private ErrorMessage(String key, Object... args) {
            this.key = key;
            this.args = args;
        }

        String getKey() {
            return key;
        }

        Object[] getArgs() {
            return args;
        }
    }
}
