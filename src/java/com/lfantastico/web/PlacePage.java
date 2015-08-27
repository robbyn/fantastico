package com.lfantastico.web;

import com.lfantastico.domain.Attendance;
import com.lfantastico.domain.Event;
import com.lfantastico.domain.MailTemplate;
import com.lfantastico.domain.User;
import com.lfantastico.mail.Mail;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.tastefuljava.mvc.Param;

public class PlacePage extends BasePage {
    private int id = -1;

    public Event getEvent() {
        if (id < 0) {
            return null;
        }
        Helper helper = Helper.current();
        return helper.getEventById(id);
    }

    public void edit(@Param("id") int id) throws IOException {
        this.id = id;
        redirect();
    }

    public void place(@Param("user") String userId,
            @Param("place") String placeId) {
        Helper helper = Helper.current();
        Event evt = getEvent();
        User user = helper.getUserById(Integer.parseInt(userId.substring(1)));
        int place = Integer.parseInt(placeId.substring(1));
        Attendance cur = evt.getAttendance(place);
        if (cur != null) {
            cur.setNumber(0);
        }
        Attendance att = evt.getAttendance(user);
        att.setNumber(place);
        helper.commit();
    }

    public void remove(@Param("user") String userId) {
        Helper helper = Helper.current();
        Event evt = getEvent();
        User user = helper.getUserById(Integer.parseInt(userId.substring(1)));
        Attendance att = evt.getAttendance(user);
        att.setNumber(0);
        helper.commit();
    }

    public void validate() throws IOException {
        Helper helper = Helper.current();
        Event evt = getEvent();
        for (Map.Entry<User,Attendance> entry:
                evt.getAttendances().entrySet()) {
            User user = entry.getKey();
            try {
                Attendance att = entry.getValue();
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("user", user);
                params.put("attendance", att);
                params.put("event", evt);
                String language = user.getFavoriteLanguage().getCode();
                String tempName;
                if (att.getNumber() > 0) {
                    tempName = "confirmation";
                    att.setState(Attendance.State.ACCEPTED);
                } else {
                    tempName = "rejection";
                    att.setState(Attendance.State.REJECTED);
                }
                MailTemplate template = helper.getMailTemplate(
                        language, tempName);
                if (template != null) {
                    Mail.send(template, params);
                }
            } catch (Exception e) {
                addError("", "sendmail-error", user.getEmail());
            }
        }
        evt.setState(Event.State.PLACED);
        helper.commit();
        redirect();
    }
}
