package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Post;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.PostsRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PostsService(PostsRepository postsRepository, UserService userService, MongoTemplate mongoTemplate) {
        this.postsRepository = postsRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }

    public Object getAll(Map<String, String> carParam) {
        List<Post> allPosts = postsRepository.findAll();
        return findPostsByCar(carParam, allPosts, null);
    }

    public Object getAllByType(PostType type, Map<String, String> carParam) {
        Optional<List<Post>> findPosts = postsRepository.findByTypeOptional(type);
        List<Post> returnPosts = findPosts.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Posts_not_found"));
        return findPostsByCar(carParam, returnPosts, type);
    }

    private Object findPostsByCar(Map<String, String> carParam, List<Post> returnPosts, PostType type) {
        if (!carParam.isEmpty()) {
            Query query = new Query();
            if (type != null) {
                query.addCriteria(Criteria.where("type").is(type));
            }
            carParam.keySet().forEach(key -> {
                query.addCriteria(Criteria.where("car." + key).is(carParam.get(key)));
            });
            returnPosts = mongoTemplate.find(query, Post.class);

        }
        return ok(returnPosts.stream().sorted(Comparator.comparing(Post::getCreatedDate).reversed()));
    }

    public Object addPost(NewPost newPost, String token) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = userService.getUserByToken(token);
        Post post = new Post();
        post.setBody(newPost.getBody());
        post.setType(newPost.getType());
        post.setTitle(newPost.getTitle());
        post.setCreatorId(currentUser.getId());
        post.setCreatedDate(new Date());
        post.setLocation(newPost.getLocation());
        post.setUserIdLikes(new ArrayList<>());
        post.setCar(newPost.getCar());
        postsRepository.save(post);
        model.put("message", "Post added successful.");
        return ok(model);
    }


    public Object deletePost(String id) {
        Map<Object, Object> model = new HashMap<>();
        postsRepository.deleteById(id);
        model.put("message", "Post " + id + " removed successful.");
        return ok(model);
    }


    public Object getPostsByCreatorId(String id) {
        Optional<List<Post>> findPosts = postsRepository.findByCreatorIdOptional(id);
        List<Post> returnPosts = findPosts.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Posts_not_found"));
        return ok(returnPosts.stream().sorted(Comparator.comparing(Post::getCreatedDate).reversed()));
    }
}
