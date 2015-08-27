package com.lfantastico.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PageVersion {
    private int id;
    private Map<String,String> strings = new HashMap<String,String>();
    private Map<String,String> texts = new HashMap<String,String>();

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
}
