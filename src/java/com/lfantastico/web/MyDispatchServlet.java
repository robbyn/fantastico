package com.lfantastico.web;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import org.tastefuljava.mvc.DispatchServlet;
import org.tastefuljava.mvc.Request;

public class MyDispatchServlet extends DispatchServlet {
    private static final Pattern STATIC_PATTERN
            = Pattern.compile("^/(ckeditor|css|images|js)/.*$");

    @Override
    protected boolean isStatic(String path) {
        if (super.isStatic(path)) {
            return true;
        }
        return STATIC_PATTERN.matcher(path).matches();
    }

    @Override
    protected void handle(Request req) throws IOException, ServletException {
        Helper helper = Helper.create(req.getServerName(), req.getRemoteUser());
        try {
            req.setAttribute("helper", helper);
            SessionHelper sess
                    = (SessionHelper)req.getSessionAttribute("session");
            if (sess == null) {
                Locale loc = req.getLocale();
                if (loc == null || loc.getLanguage() == null) {
                    loc = Locale.ENGLISH;
                }
                String language = loc.getLanguage();
                if (!helper.getSite().hasVersion(language)) {
                    language = "fr";
                }
                sess = new SessionHelper(new Locale(language));
                req.setSessionAttribute("session", sess);
            }
            super.handle(req);
        } finally {
            helper.close();
        }
    }
}
