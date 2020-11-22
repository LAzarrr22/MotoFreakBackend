package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.QuestionAnswer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Challenges")
@NoArgsConstructor
public class Challenge {

    @Id
    private String id;
    private String name;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date updatedDate;
    private boolean general;
    private String company;
    private String model;
    private String generation;
    private String creatorId;
    private List<QuestionAnswer> qaList;
    private Map<String,Integer> competitorIdList;

}
