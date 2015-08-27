package com.lfantastico.web;

import com.lfantastico.domain.Event;
import com.lfantastico.domain.User;
import java.io.IOException;
import org.tastefuljava.mvc.Param;

public class AppliedPage extends BasePage {
    private int eventId;

    public Event getEvent() {
        Helper helper = Helper.current();
        return helper.getEventById(eventId);
    }

    public void start(@Param("id") int id) throws IOException {
        Helper helper = Helper.current();
        Event event = helper.getEventById(id);
        User user = helper.getUser();
        if (event == null || user == null) {
            redirect("/main/");
            return;
        }
        eventId = id;
        redirect();
    }

    public void changePhoto() throws IOException {
        Helper helper = Helper.current();
        PhotoUpload.changePhoto(helper.getUser());
        redirect("/profile/myProfile");
    }
}
