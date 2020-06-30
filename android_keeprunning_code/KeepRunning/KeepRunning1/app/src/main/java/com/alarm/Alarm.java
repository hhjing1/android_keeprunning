package com.alarm;

public class Alarm {
    public int id;
    public String username;
    public String hour;
    public String minute;
    public String content;
    public int clockType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getClockType() {
        return clockType;
    }

    public void setClockType(int clockType) {
        this.clockType = clockType;
    }

    public Alarm(String username, String hour, String minute, String content) {
        this.username = username;
        this.hour = hour;
        this.minute = minute;
        this.content = content;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getContent() {
        return content;
    }

    public Alarm() {
    }
}