package com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserRoles")
public class UserRoles {

    @Id
    private String id;
    private final Role role;

    public UserRoles(Role role) {
        this.role = role;
    }
}
