package com.lfantastico.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MailTemplate {
    private int id;
    private String subtype;
    private String from;
    private Set<String> tos = new HashSet<String>();
    private Set<String> bccs = new HashSet<String>();
    private String subject;
    private String body;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToStr() {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (String to: tos) {
            if (first) {
                first = false;
            } else {
                buf.append(";");
            }
            buf.append(to);
        }
        return buf.toString();
    }

    public void setToStr(String newValue) {
        String array[] = newValue.split("[;]");
        for (int i = 0; i < array.length; ++i) {
            array[i] = array[i].trim();
        }
        setTos(Arrays.asList(array));
    }

    public Set<String> getTos() {
        return Collections.unmodifiableSet(tos);
    }

    public void setTos(Collection<String> newValue) {
        tos.clear();
        if (newValue != null) {
            tos.addAll(newValue);
        }
    }

    public String getBccStr() {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (String bcc: bccs) {
            if (first) {
                first = false;
            } else {
                buf.append(";");
            }
            buf.append(bcc);
        }
        return buf.toString();
    }

    public void setBccStr(String newValue) {
        String array[] = newValue.split("[;]");
        for (int i = 0; i < array.length; ++i) {
            array[i] = array[i].trim();
        }
        setBccs(Arrays.asList(array));
    }

    public Set<String> getBccs() {
        return Collections.unmodifiableSet(bccs);
    }

    public void setBccs(Collection<String> newValue) {
        bccs.clear();
        if (newValue != null) {
            bccs.addAll(newValue);
        }
    }
}
