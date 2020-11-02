package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.MemberState;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.TypeEvents;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Events")
public class Event {

    @Id
    private String id;
    private TypeEvents type;
    private String name;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date eventDate;
    private String place;
    private String description;
    private String creatorUserName;
    private Map<MemberState, List<String>> membersUserName;

    public Event(TypeEvents type, String name, Date createdDate, Date eventDate, String place, String description, String creatorUserName, Map<MemberState, List<String>> membersUserName) {
        this.type = type;
        this.name = name;
        this.createdDate = createdDate;
        this.eventDate = eventDate;
        this.place = place;
        this.description = description;
        this.creatorUserName = creatorUserName;
        this.membersUserName = membersUserName;
    }
}
