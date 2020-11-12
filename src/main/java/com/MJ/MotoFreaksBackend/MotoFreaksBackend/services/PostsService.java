package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Post;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostState;
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

    public Object getPosts(PostType type, Map<String, String> carParam, String creator) {
        List<Post> allPosts = postsRepository.findAll();
        return findPostsFilters(carParam, allPosts, type,creator);
    }

    private Object findPostsFilters(Map<String, String> carParam, List<Post> returnPosts, PostType type, String creatorId) {
        Query query = new Query();
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (creatorId != null) {
            query.addCriteria(Criteria.where("creatorId").is(creatorId));
        }
        if (!carParam.isEmpty()) {
            carParam.keySet().forEach(key -> {
                query.addCriteria(Criteria.where("car." + key).is(carParam.get(key)));
            });
            }

        returnPosts = mongoTemplate.find(query, Post.class);

        if(returnPosts.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Posts not found");
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
        post.setState(PostState.OPEN);
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

    public Object resolvePost(String id) {
        Map<Object, Object> model = new HashMap<>();
        Post post = postsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        post.setState(PostState.RESOLVED);
        postsRepository.save(post);
        model.put("message", "Post " + id + " set resolved.");
        return ok(model);
    }
}
