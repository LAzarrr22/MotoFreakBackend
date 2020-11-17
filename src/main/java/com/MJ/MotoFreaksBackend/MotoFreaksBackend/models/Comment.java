package com.MJ.MotoFreaksBackend.MotoFreaksBackend.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private String id;
    private String context;
    private String creatorId;
    private Date createdDate;
    private List<String> approved;
    private List<String> rejected;
}
