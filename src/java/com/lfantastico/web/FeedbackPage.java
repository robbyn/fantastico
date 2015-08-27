package com.lfantastico.web;

import com.lfantastico.mail.Mail;
import java.io.IOException;
import org.tastefuljava.mvc.Param;

public class FeedbackPage extends BasePage {

    @Override
    protected String getTemplate() {
        return "gen/feedback.jsp";
    }

    public void feedback(
            @Param("email") String from,
            @Param("subject") String subject,
            @Param("text") String text,
            @Param("subtype") String subtype) throws IOException {
        Mail.send(from, null, null, subject, text, subtype);
        redirect();
    }
}
