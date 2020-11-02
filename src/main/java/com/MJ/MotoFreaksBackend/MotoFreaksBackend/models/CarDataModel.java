package com.MJ.MotoFreaksBackend.MotoFreaksBackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDataModel {

    private String id;
    private Date createdDate;
    private Date updatedDate;
    private String name;
    private String registration;
    private String company;
    private String model;
    private String generation;
    private Integer year;
    private String color;
    private String engine;
    private Integer horsepower;
    private Integer torque;

}

