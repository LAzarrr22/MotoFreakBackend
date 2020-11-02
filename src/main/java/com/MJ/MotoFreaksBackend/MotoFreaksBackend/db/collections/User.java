package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Gender;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.CarDataModel;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Contact;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Message;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Document(collection = "Users")
@Data
public class User {
    @Id
    private String id;
    private String userName;
    private String password;
    private String name;
    private String lastName;
    private Gender gender;
    private boolean enabled;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date updatedDate;
    private List<Date> loginsHistory;
    private Set<UserRoles> userRoles;
    private List<CarDataModel> carsList;
    private Contact contact;
    private Address address;
    private Integer points;
    private List<String> friendsList;
    private Map<String, List<Message>> messages;


}
