package com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.request;

import lombok.Data;

@Data
public class AuthBody {

    private String username;
    private String password;
}

