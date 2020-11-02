package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChallengeDto {

    private String id;
    private String name;
    private String company;
    private String model;
    private String generation;
    private String creatorId;
    private boolean alreadyFilled;
    private int questionsCount;
    private int allPoints;
}
