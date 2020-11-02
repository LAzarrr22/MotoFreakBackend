package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Post;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends MongoRepository<Post, String> {

    @Query("{'creatorId': ?0}")
    Optional<List<Post>> findByCreatorIdOptional(String creatorId);

    @Query("{'type': ?0}")
    Optional<List<Post>> findByTypeOptional(PostType type);
}
