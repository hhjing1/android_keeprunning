package com.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class location implements Serializable
{
    public int id;
    public String username,date,time,points,distance,energy,speed;

    public location(int id, String username, String date, String time, String points, String distance, String energy, String speed) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.time = time;
        this.points = points;
        this.distance = distance;
        this.energy = energy;
        this.speed = speed;
    }
    public location(){}
    public location(String username, String date, String time, String points, String distance, String energy, String speed) {
        this.username = username;
        this.date = date;
        this.time = time;
        this.points = points;
        this.distance = distance;
        this.energy = energy;
        this.speed = speed;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoints() {
        return points;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setPoints(String points) {
        this.points = points;
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
