package com.lfantastico.web;

import com.lfantastico.domain.Event;
import com.lfantastico.domain.MailTemplate;
import com.lfantastico.domain.User;
import com.lfantastico.mail.Mail;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tastefuljava.mvc.Param;

public class EventListPage extends BasePage {
    private Date dateMin;
    private Date dateMax;

    public EventListPage() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        dateMin = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        dateMax = cal.getTime();
    }

    public Date getDateMin() {
        return dateMin;
    }

    public Date getDateMax() {
        return dateMax;
    }

    public List<Event> getQueryEvents() {
        return Helper.current().getQueryEvents(dateMin, dateMax);
    }

    public void changeDates(
            @Param(value="dateMin", pattern="dd/MM/yyyy") Date min,
            @Param(value="dateMax", pattern="dd/MM/yyyy") Date max)
            throws IOException {
        dateMin = min;
        dateMax = max;
        redirect();
    }

    public void cancel(@Param("id") int id) throws IOException {
        Helper helper = Helper.current();
        Event event = helper.getEventById(id);
        event.setState(Event.State.CANCELED);
        for (User user: event.getAttendances().keySet()) {
            user.addCredit(event.getPrice());
            MailTemplate template = helper.getMailTemplate(
                    user.getFavoriteLanguage().getCode(), "cancelation");
            if (template != null) {
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("helper", helper);
                params.put("user", user);
                params.put("password", event);
                Mail.send(template, params);
            }
        }
        helper.commit();
        redirect();
    }
}
