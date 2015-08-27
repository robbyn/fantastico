package com.lfantastico.web;

import com.lfantastico.domain.Event;
import com.lfantastico.domain.Event.Orientation;
import com.lfantastico.domain.Keyword;
import com.lfantastico.domain.Relationship;
import com.lfantastico.domain.Site;
import com.lfantastico.domain.User;
import com.lfantastico.util.Util;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.tastefuljava.mvc.JSonWriter;
import org.tastefuljava.mvc.Param;
import org.tastefuljava.mvc.QuickMap;
import org.tastefuljava.mvc.Request;

public class EventPage extends BasePage {
    private int eventId = -1;
    private String date = "";
    private String time = "";
    private String location = "";
    private Event.Orientation orientation = Event.Orientation.HETERO;
    private int minAge = 7;
    private int maxAge = 77;
    private String relationship = "any";
    private String language = "fr";
    private BigDecimal price = BigDecimal.valueOf(50);
    private String address = "";
    @Param("keyword")
    private int keywordIds[] = {};
    private int maxAttendees = 16;

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getTime() {
        return time;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public User getUser() {
        return Helper.current().getUser();
    }

    public Keyword[] getAllKeywords() {
        Helper helper = Helper.current();
        Site site = helper.getSite();
        Set<Keyword> set = site.getKeywords();
        Keyword result[] = set.toArray(new Keyword[set.size()]);
        Arrays.sort(result, new Keyword.TextOrder(getLanguage()));
        return result;
    }

    public boolean hasKeyword(Keyword kw) {
        int kwid = kw.getId();
        for (int id: keywordIds) {
            if (id == kwid) {
                return true;
            }
        }
        return false;
    }

    public Map<Keyword, Boolean> getKeywordMap() {
        return new QuickMap<Keyword,Boolean>() {
            @Override
            public Boolean get(Object key) {
                if (key instanceof Keyword) {
                    return hasKeyword((Keyword)key);
                }
                return false;
            }
        };
    }

    public void getKeywordx(@Param("id") int id) throws IOException {
        Helper helper = Helper.current();
        Keyword kw = helper.getKeywordById(id);
        jsonKeyword(kw, false);
    }

    public void saveKeyword(@Param("id") int id) throws IOException {
        Helper helper = Helper.current();
        Keyword kw = id < 0 ? new Keyword() : helper.getKeywordById(id);
        if (kw != null) {
            Request req = Request.current();
            for (String lc: helper.getSite().getSupportedLanguages()) {
                kw.setText(lc, req.getParameter("text_" + lc));
            }
            if (id < 0) {
                helper.getSite().addKeyword(kw);
            }
            helper.commit();
        }
        jsonKeyword(kw, id < 0);
    }

    public void create() throws IOException {
        edit(-1);
    }

    public void edit(@Param("id") int id) throws IOException {
        Helper helper = Helper.current();
        Event event = helper.getEventById(id);
        if (event == null) {
            eventId = -1;
            date = "";
            time = "";
            location = "";
            orientation = Event.Orientation.HETERO;
            minAge = 7;
            maxAge = 77;
            relationship = "any";
            language = "fr";
            price = BigDecimal.valueOf(50);
            address = "";
            keywordIds = new int[] {};
            maxAttendees = 16;
        } else {
            eventId = id;
            date = Util.formatDate(event.getDateTime());
            time = Util.formatDateTime(event.getDateTime(), "HH:mm");
            location = event.getLocation();
            orientation = event.getOrientation();
            minAge = event.getAgeRange().getMinAge();
            maxAge = event.getAgeRange().getMaxAge();
            relationship = event.getRelationship() == null
                    ? "any" : event.getRelationship().getName();
            language = event.getLanguage() == null
                    ? "fr" : event.getLanguage().getCode();
            price = event.getPrice();
            address = event.getAddress();
            Set<Keyword> keywords = event.getKeywords();
            keywordIds = new int[keywords.size()];
            int i = 0;
            for (Keyword kw: keywords) {
                keywordIds[i] = kw.getId();
                ++i;
            }
            maxAttendees = event.getMaxAttendees();
        }
        redirect();
    }

    public void save() throws IOException {
        Request.current().save(this);
        Helper helper = Helper.current();
        Event event = eventId < 0 ? new Event() : helper.getEventById(eventId);
        event.setDateTime(Util.parseDateTime(date + " " + time));
        event.setLocation(location);
        event.setOrientation(orientation);
        event.setAgeRange(minAge, maxAge);
        event.setRelationship("any".equals(relationship) ? null
                : Relationship.valueOf(relationship));
        event.setLanguage(helper.getLanguage(language));
        event.setPrice(price);
        event.setAddress(address);
        Set<Keyword> keywords = new HashSet<Keyword>();
        for (int id: keywordIds) {
            Keyword kw = helper.getKeywordById(id);
            if (kw != null) {
                keywords.add(kw);
            }
        }
        event.setKeywords(keywords);
        event.setMaxAttendees(maxAttendees);
        if (!hasErrors()) {
            helper.saveOrUpdate(event);
            helper.commit();
        }
        redirect();
    }

    private void jsonKeyword(Keyword kw, boolean isNew) throws IOException {
        JSonWriter out = Request.current().getJSonWriter("UTF-8", true);
        try {
            out.startObject();
            if (kw != null) {
                out.printField("id", kw.getId());
                out.printField("checked", hasKeyword(kw));
                out.printField("isNew", isNew);
                out.startObjectField("texts");
                for (Map.Entry<String,String> entry: kw.getTexts().entrySet()) {
                    out.printField(entry.getKey(), entry.getValue());
                }
                out.endField();
            }
            out.endBlock();
        } finally {
            out.close();
        }
    }
}
