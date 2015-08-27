package com.lfantastico.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class State {
    public static final Comparator<State> CODE_ORDER;
    public static final Comparator<State> NAME_ORDER;

    private static final Logger LOG = LoggerFactory.getLogger(State.class);
    private static final Map<String,State> STATES;

    private String code;
    private String name;

    public static State get(String code) {
        return STATES.get(code);
    }

    public static List<State> getAll() {
        List<State> result = new ArrayList<State>(STATES.values());
        Collections.sort(result, NAME_ORDER);
        return result;
    }

    private State(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    static {
        try {
            STATES = new HashMap<String,State>();
            Properties props = new Properties();
            InputStream in = State.class.getResourceAsStream("states.properties");
            try {
                props.load(in);
            } finally {
                in.close();
            }
            for (String code: props.stringPropertyNames()) {
                STATES.put(code, new State(code, props.getProperty(code)));
            }
        } catch (IOException e) {
            LOG.error("Error loading state file", e);
            throw new RuntimeException(e.getMessage());
        }
        CODE_ORDER = new Comparator<State>() {
            public int compare(State o1, State o2) {
                return o1.code.compareTo(o2.code);
            }
        };
        NAME_ORDER = new Comparator<State>() {
            public int compare(State o1, State o2) {
                return o1.name.compareTo(o2.name);
            }
        };
    }
}
