package com.lfantastico.web;

import com.lfantastico.mail.Mail;
import com.lfantastico.domain.MailTemplate;
import com.lfantastico.domain.User;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends ProfilePage {
    @Override
    protected void afterSave(String password) throws IOException {
        notifyRegistration(password);
        redirect("/j_security_check?j_username="
                + URLEncoder.encode(getEmail(), "UTF-8")
                + "&j_password="
                + URLEncoder.encode(password, "UTF-8"));
    }

    private void notifyRegistration(String password) {
        Helper helper = Helper.current();
        User user = helper.getUserById(getUserId());
        MailTemplate template = helper.getMailTemplate(
                user.getFavoriteLanguage().getCode(), "registration");
        if (template != null) {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("helper", helper);
            params.put("user", user);
            params.put("password", password);
            Mail.send(template, params);
        }
    }
}
