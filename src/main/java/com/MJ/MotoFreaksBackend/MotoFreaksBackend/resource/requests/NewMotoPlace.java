package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.TypePlace;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Review;
import lombok.Data;

@Data
public class NewMotoPlace {

    private TypePlace type;
    private String name;
    private Address address;
    private String webPageUrl;
    private String creatorId;
    private Review reviewList;
}
