package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.UserRoles;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends MongoRepository<UserRoles, String> {
    Optional<UserRoles> findByRole(Role role);

}
