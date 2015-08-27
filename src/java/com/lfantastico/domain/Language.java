package com.lfantastico.domain;

import java.util.HashMap;
import java.util.Map;

public class Language {
    private String code;
    private Map<String,String> labels = new HashMap<String,String>();

    /**
     * @deprecated should not be called directly. It's only use is via
     * serialization or hibernate.
     */
    @Deprecated()
    public Language() {
    }

    public Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getLabel(String language) {
        return labels.get(language);
    }

    public void setLabel(String language, String value) {
        labels.put(language, value);
    }

    public Map<String,String> getLabels() {
        return new HashMap<String,String>(labels);
    }

    public void setLabels(Map<String,String> newValue) {
        labels.clear();
        if (newValue != null) {
            labels.putAll(newValue);
        }
    }

    public String getNativeLabel() {
        return labels.get(code);
    }

    public String getDefaultLabel() {
        String result = getNativeLabel();
        if (result == null && !labels.isEmpty()) {
            result = labels.get("en");
            if (result == null) {
                result = labels.get("fr");
                if (result == null) {
                    result = labels.values().iterator().next();
                }
            }
        }
        return result;
    }
}
