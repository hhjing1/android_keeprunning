package com.user;

public class User {

    public int id;
    public String username;
    public String userpsw;
    public String userpicture;
    public String usercheck;
    public String nicheng;
    public String qianming;
    public String health;


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

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public String getQianming() {
        return qianming;
    }

    public void setQianming(String qianming) {
        this.qianming = qianming;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
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

    public User(String username, String userpsw, String userpicture, String nicheng, String qianming,String health) {
        this.username = username;
        this.userpsw = userpsw;
        this.userpicture = userpicture;
        this.nicheng = nicheng;
        this.qianming = qianming;
        this.health=health;
    }

}
