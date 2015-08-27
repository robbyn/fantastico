package com.lfantastico.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
    private String id;
    private Payment parent;
    private Date dateTime;
    private BigDecimal amount;
    private User user;
    private Event event;
    private String details;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User customer) {
        this.user = customer;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payment getParent() {
        return parent;
    }

    public void setParent(Payment parent) {
        this.parent = parent;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
