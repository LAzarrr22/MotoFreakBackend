package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Sentence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SentenceRepository extends MongoRepository<Sentence, String> {
}
