package com.example.myapplication.Models;

public class Historic {
    private String date;
    private String hour;
    private Double price;
    private String transportation;

    public Historic(String date, String hour, Double price, String transportation) {
        this.date = date;
        this.hour = hour;
        this.price = price;
        this.transportation = transportation;
    }
    public Historic(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }
}
