package org.tastefuljava.mvc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public abstract class Page implements HttpSessionBindingListener {
    private String name;
    private HttpSession session;

    public Page() {
    }

    public String getName() {
        return name;
    }

    void setName(String newValue) {
        name = newValue;
    }

    public HttpSession getSession() {
        return session;
    }

    protected String getTemplate() {
        return name + ".jsp";
    }

    public void forward() throws ServletException, IOException {
        forward(getTemplate());
    }

    public void forward(String pageName) throws ServletException, IOException {
        Request.current().forward(pageName);
    }

    public void redirect(String url) throws IOException {
        Request.current().redirect(url);
    }

    public void redirect() throws IOException {
        Request.current().redirect();
    }

    public final void valueBound(HttpSessionBindingEvent event) {
        session = event.getSession();
        bound();
    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        unbound();
        session = null;
    }

    protected void bound() {
    }

    protected void unbound() {
    }
}
