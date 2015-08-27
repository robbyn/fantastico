package com.lfantastico.web;

import java.io.IOException;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tastefuljava.mvc.Param;

public class RssPage extends BasePage {
    private static final Logger LOG
            = LoggerFactory.getLogger(RssPage.class);

    @Override
    protected String getTemplate() {
        return "rss.jsp";
    }

    public void get(@Param("language") String language)
            throws IOException, ServletException {
        getSess().setLanguage(language);
        forward();
    }
}
