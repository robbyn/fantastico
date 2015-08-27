package com.lfantastico.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Site {
    private int id;
    private String hostName;
    private Map<String,SiteVersion> versions
            = new HashMap<String,SiteVersion>();
    private Set<Keyword> keywords = new HashSet<Keyword>();

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String newValue) {
        hostName = newValue;
    }

    public Set<String> getSupportedLanguages() {
        return Collections.unmodifiableSet(versions.keySet());
    }

    public Map<String,SiteVersion> getVersions() {
        return Collections.unmodifiableMap(versions);
    }

    public void setVersions(Map<String,SiteVersion> newValue) {
        versions.clear();
        if (newValue != null) {
            versions.putAll(newValue);
        }
    }

    public boolean hasVersion(String language) {
        return versions.containsKey(language);
    }

    public SiteVersion getVersion(String language) {
        return versions.get(language);
    }

    public void setVersion(String language, SiteVersion version) {
        if (version == null) {
            versions.remove(language);
        } else {
            versions.put(language, version);
        }
    }

    public Set<Keyword> getKeywords() {
        return Collections.unmodifiableSet(keywords);
    }

    public void addKeyword(Keyword kw) {
        keywords.add(kw);
    }

    public void removeKeyword(Keyword kw) {
        keywords.remove(kw);
    }
}
