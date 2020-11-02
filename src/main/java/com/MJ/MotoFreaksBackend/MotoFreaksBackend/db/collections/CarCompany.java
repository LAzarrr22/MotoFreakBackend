package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "CarsCompany")
@Data
public class CarCompany {

    @Id
    private String id;
    private Date createdDate;
    private Date updatedDate;
    private String creatorId;
    private String company;
    private Map<String, List<String>> modelList;


    public CarCompany(String company, Map<String, List<String>> modelList) {
        this.company = company;
        this.modelList = modelList;
    }
}
