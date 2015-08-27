package com.lfantastico.web;

import com.lfantastico.domain.Attendance;
import com.lfantastico.domain.Event;
import com.lfantastico.domain.Payment;
import com.lfantastico.domain.User;
import com.lfantastico.util.Util;
import com.lfantastico.web.pay.PaymentButton;
import com.lfantastico.web.pay.PaymentInfo;
import com.lfantastico.web.pay.PaymentService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tastefuljava.mvc.Param;
import org.tastefuljava.mvc.Request;

public class ApplyPage extends BasePage {
    private static final Logger LOG
            = LoggerFactory.getLogger(RegistrationPage.class);

    private PaymentButton button;
    private int eventId;

    public Event getEvent() {
        Helper helper = Helper.current();
        return helper.getEventById(eventId);
    }

    public PaymentButton getPaymentButton() {
        return button;
    }

    @Override
    protected String getTemplate() {
        Helper helper = Helper.current();
        Event event = helper.getEventById(eventId);
        User user = helper.getUser();
        if (event != null && user != null
                && event.getAttendance(user) != null) {
            return "already-applied.jsp";
        } else {
            return super.getTemplate();
        }
    }

    public void start(@Param("id") int id) throws IOException {
        Helper helper = Helper.current();
        Request req = Request.current();
        Event event = helper.getEventById(id);
        User user = helper.getUser();
        if (event == null || user == null) {
            redirect("/main/");
            return;
        }
        eventId = id;
        if (event.getAttendance(user) != null) {
            redirect();
        } else if (user.getCredit().compareTo(event.getPrice()) < 0) {
            if (button == null) {
                PaymentService service = PaymentService.getInstance();
                button = service.createButton();
            }
            button.setLabel(getString("payment-button"));
            button.setCustomerId(user.getEmail());
            button.setItemCode(Integer.toString(event.getId()));
            button.setItemLabel(getEventSummary(event));
            button.setPrice(event.getPrice().subtract(user.getCredit()));
            button.setCurrency("CHF");
            button.setReturnUrl(req.getBaseUrl() + "/apply/done");
            button.setCancelUrl(req.getBaseUrl() + "/apply/cancel");
            button.setNotifyUrl(req.getBaseUrl() + "/apply/notification");
            button.setEmail(user.getEmail());
            button.setFirstName(user.getFirstName());
            button.setLastName(user.getLastName());
            button.setAddress1(user.getAddress1());
            button.setAddress2(user.getAddress2());
            button.setZip(user.getZip());
            button.setCity(user.getCity());
            button.setState(user.getState());
            button.setCountry(user.getCountry());
            redirect();
        } else {
            event.addAttendance(user, new Attendance());
            user.subtractCredit(event.getPrice());
            helper.commit();
            redirect("/applied/start?id=" + eventId);
        }
    }

    public void done(@Param("tx") String tx) throws IOException {
        if (tx != null) {
            PaymentService service = PaymentService.getInstance();
            PaymentInfo pi = service.getPaymentInfo(tx);
            processPayment(pi);
        }
        redirect("/applied/start?id=" + eventId);
    }

    public void cancel() throws IOException {
        redirect("/main/");
    }

    public void notification() throws IOException {
        Request req = Request.current();
        PaymentService service = PaymentService.getInstance();
        PaymentInfo pi = service.processNotification(req);
        processPayment(pi);
    }

    private String getEventSummary(Event event) {
        return event.getLocation() + " " + Util.formatDate(event.getDateTime());
    }

    private void processPayment(PaymentInfo pi) {
        Helper helper = Helper.current();
        Payment payment = helper.getPayment(pi.getTransactionId());
        if (payment != null) {
            LOG.info("Payment already registered " + pi.getTransactionId());
        } else {
            User user = helper.getUser(pi.getCustomerId());
            int evid = Integer.parseInt(pi.getItemCode());
            Event event = helper.getEventById(evid);
            payment = new Payment();
            payment.setId(pi.getTransactionId());
            payment.setDateTime(pi.getDate());
            payment.setUser(user);
            payment.setEvent(event);
            payment.setAmount(pi.getPrice());
            payment.setDetails(pi.getDetails());
            helper.save(payment);
            user.addCredit(payment.getAmount());
            if (event != null && event.getAttendance(user) == null) {
                event.addAttendance(user, new Attendance());
                user.subtractCredit(event.getPrice());
            }
            helper.commit();
        }
    }
}
