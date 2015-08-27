package com.lfantastico.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SiteVersion {
    private int id;
    private Map<String,String> strings = new HashMap<String,String>();
    private Map<String,String> texts = new HashMap<String,String>();
    private Map<String,PageVersion> pages = new HashMap<String,PageVersion>();
    private Map<String,MailTemplate> mailTemplates
            = new HashMap<String,MailTemplate>();

    public int getId() {
        return id;
    }

    public Map<String,String> getStrings() {
        return Collections.unmodifiableMap(strings);
    }

    public void setStrings(Map<String,String> newValue) {
        strings.clear();
        if (newValue != null) {
            strings.putAll(newValue);
        }
    }

    public String getString(String key) {
        return strings.get(key);
    }

    public void setString(String key, String value) {
        if (value == null) {
            strings.remove(key);
        } else {
            strings.put(key, value);
        }
    }

    public Map<String,String> getTexts() {
        return Collections.unmodifiableMap(texts);
    }

    public void setTexts(Map<String,String> newValue) {
        texts.clear();
        if (newValue != null) {
            texts.putAll(newValue);
        }
    }

    public String getText(String key) {
        return texts.get(key);
    }

    public void setText(String key, String value) {
        if (value == null) {
            texts.remove(key);
        } else {
            texts.put(key, value);
        }
    }

    public Map<String,PageVersion> getPages() {
        return Collections.unmodifiableMap(pages);
    }

    public void setPages(Map<String,PageVersion> newValue) {
        pages.clear();
        if (newValue != null) {
            pages.putAll(newValue);
        }
    }

    public PageVersion getPage(String name) {
        return pages.get(name);
    }

    public void setPage(String name, PageVersion page) {
        if (page == null) {
            pages.remove(name);
        } else {
            pages.put(name, page);
        }
    }

    public Map<String,MailTemplate> getMailTemplates() {
        return Collections.unmodifiableMap(mailTemplates);
    }

    public void setMailTemplates(Map<String,MailTemplate> newValue) {
        mailTemplates.clear();
        if (newValue != null) {
            mailTemplates.putAll(newValue);
        }
    }

    public MailTemplate getMailTemplate(String name) {
        return mailTemplates.get(name);
    }

    public void setMailTemplate(String name, MailTemplate template) {
        if (template == null) {
            mailTemplates.remove(name);
        } else {
            mailTemplates.put(name, template);
        }
    }
}
