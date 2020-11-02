package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.CarDataModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Posts")
public class Post {

    @Id
    private String id;
    private PostType type;
    private String title;
    private String body;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
    private String creatorId;
    private List<String> userIdLikes;
    private Address location;
    private CarDataModel car;

}
