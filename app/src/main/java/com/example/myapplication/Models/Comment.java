package com.example.myapplication.Models;

public class Comment {
    private String author;
    private String message;
    private String id_transport;

    public Comment(){}

    public Comment(String author, String message, String id_transport) {
        this.author = author;
        this.message = message;
        this.id_transport = id_transport;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_transport() {
        return id_transport;
    }

    public void setId_transport(String id_transport) {
        this.id_transport = id_transport;
    }
}
