package com.example.myapplication.Models;

public class Questions {
    private String questions;
    private String answer;

    public Questions(String questions, String answer) {
        this.questions = questions;
        this.answer = answer;
    }
    public Questions(){}

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
