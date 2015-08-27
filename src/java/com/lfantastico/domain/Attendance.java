package com.lfantastico.domain;

public class Attendance {
    private Event event;
    private User user;
    private int number;
    private State state = State.REQUESTED;

    public enum State {
        REQUESTED, ACCEPTED, REJECTED, COMPLETED, WITHDRAWN, ABSENT;

        public String getName() {
            return name();
        }
    };

    public Attendance() {
    }

    public Attendance(User user, Event event, Attendance other) {
        state = other.getState();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(State newValue) {
        this.state = newValue;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
