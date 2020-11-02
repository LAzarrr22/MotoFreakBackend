package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class NewCarCompany {
    private String company;
    private Map<String, List<String>> modelList;
}
