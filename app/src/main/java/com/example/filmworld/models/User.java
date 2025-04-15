package com.example.filmworld.models;

public class User {
    private String email;
    private String phone;

    public User() {
    }
    public User(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

}