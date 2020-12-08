package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests;

import lombok.Data;

@Data
public class RegisterBody {
    private String username;
    private String password;
    private String name;
    private String lastName;
    private String email;

}
