package com.example.yazlab3;

public class Kullanici {
    String username,password,place;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Kullanici(String username, String password, String place){
        this.password=password;
        this.place=place;
        this.username=username;
    }
}
