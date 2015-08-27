package com.lfantastico.web;

import com.lfantastico.domain.Event;
import java.io.IOException;
import org.tastefuljava.mvc.Param;
import org.tastefuljava.mvc.Request;

public class MainPage extends BasePage {

    public Paginator<Event> getPaginator() {
        UpcomingPage up = getUpcomingPage();
        return up.getPaginator();
    }

    public void setPage(@Param("page") int page) throws IOException {
        UpcomingPage up = getUpcomingPage();
        up.setPage(page);
        redirect();
    }

    public void start() throws IOException {
        UpcomingPage up = getUpcomingPage();
        up.setPage(0);
        redirect();
    }

    private UpcomingPage getUpcomingPage() {
        Request req = Request.current();
        return (UpcomingPage) req.getPage("upcoming");
    }
}
