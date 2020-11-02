package com.MJ.MotoFreaksBackend.MotoFreaksBackend.models;

import lombok.Data;

import java.util.List;

@Data
public class QuestionAnswer {

    private String question;
    private int points;
    private List<String> answers;
    private String correctAnswer;
}

