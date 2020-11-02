package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.QuestionAnswer;
import lombok.Data;

import java.util.List;

@Data
public class NewChallengeModel {
    private String name;
    private String company;
    private String model;
    private String generation;
    private List<QuestionAnswer> qaList;
}
