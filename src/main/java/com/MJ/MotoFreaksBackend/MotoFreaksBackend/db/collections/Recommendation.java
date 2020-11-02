package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.TypeRecomendation;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Review;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Recommendations")
public class Recommendation {

    @Id
    private String id;

    private TypeRecomendation type;
    private String name;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;

    private String creatorId;

    private List<Review> reviewList;

    public Recommendation(TypeRecomendation type, String name, Date createdDate, Date updatedDate, String creatorId, List<Review> reviewList) {
        this.type = type;
        this.name = name;
        this.createdDate = createdDate;
        this.creatorId = creatorId;
        this.reviewList = reviewList;
    }
}
