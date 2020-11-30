package com.MJ.MotoFreaksBackend.MotoFreaksBackend.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Review {

    private String id;
    private String body;
    private List<PointsReviews> pointsReviewsUsers;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
    private String creatorId;
    private Integer AvgPoints;

}
