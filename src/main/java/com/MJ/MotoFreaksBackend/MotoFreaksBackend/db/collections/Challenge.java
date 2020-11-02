package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.QuestionAnswer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Challenges")
@NoArgsConstructor
public class Challenge {

    @Id
    private String id;
    private String name;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
    private String company;
    private String model;
    private String generation;
    private String creatorId;
    private List<QuestionAnswer> qaList;
    private List<String> competitorIdList;

}
