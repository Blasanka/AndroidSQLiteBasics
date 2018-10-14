package com.bla.androidsqlitebasics.model;

public class User {
    private long id;
    private String username;
    private String password;
    private String dob;
    private String gender;

    public User() {
    }

    public User(long id, String username, String password, String dob, String gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }

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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
