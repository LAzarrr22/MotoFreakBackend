package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    @Query("{'type': ?0}")
    Optional<Event> findEventByType(Type type);
}
