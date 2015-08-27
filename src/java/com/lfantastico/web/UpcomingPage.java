package com.lfantastico.web;

import com.lfantastico.domain.Event;
import java.io.IOException;

public class UpcomingPage extends BasePage {
    private int currentPage;

    public Paginator<Event> getPaginator() {
        Helper helper = Helper.current();
        Paginator<Event> ptor = new Paginator<Event>();
        ptor.setPaginable(helper.getNextEvents());
        ptor.setCurrentPage(currentPage);
        return ptor;
    }

    public void setPage(int page) throws IOException {
        currentPage = page;
    }
}
