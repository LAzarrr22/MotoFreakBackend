package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.CarDataModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPost {

    private PostType type;
    private String title;
    private String body;
    private Address location;
    private CarDataModel car;
}
