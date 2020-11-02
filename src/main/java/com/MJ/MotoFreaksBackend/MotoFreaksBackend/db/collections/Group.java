package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.MemberState;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Groups")
public class Group {

    @Id
    private String id;
    private String name;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date updatedDate;
    private String company;
    private String model;
    private String generation;
    private Map<MemberState, List<String>> membersUsersId;
    private String creatorId;

    public Group(String id, String name, Date createdDate, Date updatedDate, String company, String model, String generation, Map<MemberState, List<String>> membersUsersId, String creatorId) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.company = company;
        this.model = model;
        this.generation = generation;
        this.membersUsersId = membersUsersId;
        this.creatorId = creatorId;
    }
}
