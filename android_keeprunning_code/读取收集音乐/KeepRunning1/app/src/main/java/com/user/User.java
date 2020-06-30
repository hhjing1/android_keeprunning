package com.user;

public class User {

    public int id;
    public String username;
    public String userpsw;
    public String userpicture;
    public String usercheck;

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

    public String getUserpsw() {
        return userpsw;
    }

    public void setUserpsw(String userpsw) {
        this.userpsw = userpsw;
    }

    public String getUserpicture() {
        return userpicture;
    }

    public void setUserpicture(String userpicture) {
        this.userpicture = userpicture;
    }

    public String getUsercheck() {
        return usercheck;
    }

    public void setUsercheck(String usercheck) {
        this.usercheck = usercheck;
    }

    public User() {
    }

    public User(String username, String userpsw, String userpicture, String usercheck) {
        this.username = username;
        this.userpsw = userpsw;
        this.userpicture = userpicture;
        this.usercheck = usercheck;
    }

    public User(String username, String usercheck) {
        this.username = username;
        this.usercheck = usercheck;
    }
}
