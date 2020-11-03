package com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.request;

import lombok.Data;

@Data
public class RegisterBody {
    private String username;
    private String password;
    private String name;
    private String lastName;
    private String email;

}
