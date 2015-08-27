package com.lfantastico.web;

import java.util.Locale;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class SessionHelper implements HttpSessionBindingListener {
    private Locale locale = Locale.FRENCH;
    private String username;
    private boolean editMode;

    public SessionHelper(Locale locale) {
        this.locale = locale;
    }

    public boolean getEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale newValue) {
        locale = newValue;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String newValue) {
        locale = new Locale(newValue);
    }

    public void valueBound(HttpSessionBindingEvent event) {

    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        
    }
}
