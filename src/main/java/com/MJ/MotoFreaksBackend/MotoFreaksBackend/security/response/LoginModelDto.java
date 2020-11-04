package com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.response;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginModelDto {
    private String username;
    private boolean isValidated;
    private String token;
    private List<Role> roles;

}
