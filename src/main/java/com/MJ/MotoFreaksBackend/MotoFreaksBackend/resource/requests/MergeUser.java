package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MergeUser {

    private String name;
    private String lastName;
    private Gender gender;
    private boolean enabled;
    private String password;
}
