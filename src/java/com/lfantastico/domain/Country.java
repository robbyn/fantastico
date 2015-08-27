package com.lfantastico.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Country {
    private String code;
    private Map<String,String> labels = new HashMap<String,String>();

    /**
     * @deprecated should not be called directly. It's only use is via
     * serialization or hibernate.
     */
    @Deprecated()
    public Country() {
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
        return Collections.unmodifiableMap(labels);
    }

    public void setLabels(Map<String,String> newValue) {
        labels.clear();
        if (newValue != null) {
            labels.putAll(newValue);
        }
    }
}
