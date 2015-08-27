package com.lfantastico.web;

import com.lfantastico.web.pay.PaymentService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.tastefuljava.mvc.PageRegistry;

public class AppListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        PageRegistry reg = PageRegistry.getInstance(sce.getServletContext());
        reg.register("apply", ApplyPage.class);
        reg.register("applied", AppliedPage.class);
        reg.register("contact", FeedbackPage.class);
        reg.register("email", EmailPage.class);
        reg.register("event-list", EventListPage.class);
        reg.register("event", EventPage.class);
        reg.register("faq", FeedbackPage.class);
        reg.register("info", SimplePage.class);
        reg.register("login", LoginPage.class);
        reg.register("main", MainPage.class);
        reg.register("photo", PhotoPage.class);
        reg.register("place", PlacePage.class);
        reg.register("profile", ProfilePage.class);
        reg.register("reg", RegistrationPage.class);
        reg.register("rss", RssPage.class);
        reg.register("user-list", UserListPage.class);
        reg.register("browser", BrowserPage.class);
        reg.register("upcoming", UpcomingPage.class);
        PaymentService.initialize();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        PaymentService.terminate();
    }
}
