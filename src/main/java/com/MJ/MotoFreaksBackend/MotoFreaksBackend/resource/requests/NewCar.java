package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewCar {
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
