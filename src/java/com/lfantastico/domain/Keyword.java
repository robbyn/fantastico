package com.lfantastico.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Keyword {
    private int id;
    private Map<String,String> texts = new HashMap<String,String>();

    public int getId() {
        return id;
    }

    public String getText(String language) {
        return texts.get(language);
    }

    public void setText(String language, String text) {
        if (text == null) {
            texts.remove(language);
        } else {
            texts.put(language, text);
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

    public static class TextOrder implements Comparator<Keyword> {
        private String language;

        public TextOrder(String language) {
            this.language = language;
        }

        public int compare(Keyword kw1, Keyword kw2) {
            String s1 = kw1.getText(language);
            String s2 = kw2.getText(language);
            if (s1 == null && s2 == null) {
                return 0;
            } else if (s1 == null) {
                return -1;
            } else if (s2 == null) {
                return 1;
            } else {
                return s1.compareToIgnoreCase(s2);
            }
        }
    }
}
