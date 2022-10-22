package com.example.myapplication.Models;

public class Passenger {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private String photo;
    private String direction;

    public Passenger(String id, String name, String surname, String email, String password, String phone, String direction, String photo) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.direction = direction;
        this.photo = photo;
    }
    public Passenger(){}
// Get and Set

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhoto(String photo) { return photo; }

    public void setPhoto(String photo) { this.photo = photo; }
}
