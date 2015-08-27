package com.lfantastico.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Event {
    private int id;
    private Date creation = new Date();
    private Date dateTime = new Date();
    private String location;
    private State state = State.SCHEDULED;
    private Orientation orientation = Orientation.HETERO;
    private AgeRange ageRange = new AgeRange(7, 77);
    private Relationship relationship;
    private Set<Keyword> keywords = new HashSet<Keyword>();
    private Language language;
    private Map<User,Attendance> attendances = new HashMap<User,Attendance>();
    private String address = "";
    private BigDecimal price = BigDecimal.valueOf(50);
    private int maxAttendees = 16;

    public enum State {
        SCHEDULED, PLACED, COMPLETED, CANCELED;

        public String getName() {
            return name();
        }
    }

    public enum Orientation {
        HETERO, GAY, LESBIAN;

        public String getName() {
            return name();
        }
    }

    public Event() {
    }

    public Event(Event other) {
        id = other.getId();
        dateTime = other.getDateTime();
        location = other.getLocation();
        state = other.getState();
        orientation = other.getOrientation();
        ageRange = other.getAgeRange();
        relationship = other.getRelationship();
        language = other.getLanguage();
        for (Map.Entry<User,Attendance> entry:
                other.getAttendances().entrySet()) {
            User user = entry.getKey();
            Attendance att = entry.getValue();
            attendances.put(user, new Attendance(user, this, att));
        }
        address = other.getAddress();
        price = other.getPrice();
        maxAttendees = other.getMaxAttendees();
    }

    public int getId() {
        return id;
    }

    public Date getCreation() {
        return creation;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public void setAgeRange(int min, int max) {
        setAgeRange(new AgeRange(min, max));
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship newValue) {
        this.relationship = newValue;
    }

    public boolean canAttend(User user) {
        switch (orientation) {
            case GAY:
                return user.getSex() == User.Sex.MALE;
            case LESBIAN:
                return user.getSex() == User.Sex.FEMALE;
        }
        return true;
    }

    public Map<User, Attendance> getAttendances() {
        return Collections.unmodifiableMap(attendances);
    }

    public void setAttendances(Map<User, Attendance> newValue) {
        attendances.clear();
        if (newValue != null) {
            attendances.putAll(newValue);
        }
    }

    public Attendance getAttendance(int number) {
        for (Attendance att: attendances.values()) {
            if (att.getNumber() == number) {
                return att;
            }
        }
        return null;
    }

    public User getUserAtPlace(int number) {
        for (Map.Entry<User,Attendance> entry: attendances.entrySet()) {
            User user = entry.getKey();
            Attendance att = entry.getValue();
            if (att.getNumber() == number) {
                return user;
            }
        }
        return null;
    }

    public Attendance getAttendance(User user) {
        return attendances.get(user);
    }

    public void addAttendance(User user, Attendance att) {
        attendances.put(user, att);
        att.setEvent(this);
        att.setUser(user);
    }

    public void removeAttendance(User user) {
        attendances.remove(user);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Keyword> getKeywords() {
        return Collections.unmodifiableSet(keywords);
    }

    public void setKeywords(Set<Keyword> newValue) {
        keywords.clear();
        if (newValue != null) {
            keywords.addAll(newValue);
        }
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(int maxAttendees) {
        if (maxAttendees != this.maxAttendees) {
            int oldcc = getCoupleCount();
            this.maxAttendees = maxAttendees;
            int newcc = getCoupleCount();
            for (Attendance att: attendances.values()) {
                int num = att.getNumber();
                if (num > oldcc && num <= newcc) {
                    att.setNumber(0);
                } else if (num > oldcc) {
                    num = num - oldcc + newcc;
                    if (num <= maxAttendees) {
                        att.setNumber(num);
                    } else {
                        att.setNumber(0);
                    }
                }
            }
        }
    }

    public int getCoupleCount() {
        return (maxAttendees+1)/2;
    }

    public boolean isPassed() {
        return dateTime.before(new Date());
    }

    public boolean getCanApply() {
        return state == State.SCHEDULED && !isPassed();
    }

    public boolean getCanEdit() {
        return state == State.SCHEDULED && !isPassed();
    }

    public boolean getCanPlace() {
        return state == State.SCHEDULED && !isPassed();
    }

    public boolean getCanCancel() {
        return state == State.SCHEDULED || state == State.PLACED;
    }
}
