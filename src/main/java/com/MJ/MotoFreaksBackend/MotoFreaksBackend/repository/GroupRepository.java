package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
}
