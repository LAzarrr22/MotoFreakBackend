package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'userName': ?0}")
    Optional<User> findByUserNameOptional(String userName);

}
