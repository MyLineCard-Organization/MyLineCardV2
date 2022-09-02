package com.example.myapplication.Models;

public class Transportation {
    private String id;
    private String name;
    private String code;
    private String price;

    public Transportation(String id, String name, String code, String price) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
    }
    public Transportation() {
    }
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
