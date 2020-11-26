package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Post;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostState;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Comment;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.PostsRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewMessage;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewPost;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

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

    public Object getPosts(PostType type, Map<String, String> reqParams, String creator) {
        List<Post> allPosts = postsRepository.findAll();
        return findPostsFilters(reqParams, allPosts, type,creator);
    }

    private Object findPostsFilters(Map<String, String> reqParams, List<Post> returnPosts, PostType type, String creatorId) {
        Query query = new Query();
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (creatorId != null) {
            query.addCriteria(Criteria.where("creatorId").is(creatorId));
        }
        if (!reqParams.isEmpty() &&reqParams.get("state")!=null) {
            if(!reqParams.get("state").equals("ALL")){
                query.addCriteria(Criteria.where("state").is(reqParams.get("state")));
            }
            reqParams.remove("state");
        }

        if (!reqParams.isEmpty()) {
            reqParams.keySet().forEach(key -> {
                if(key.equals("company")|| key.equals("model")|| key.equals("generation")){
                    query.addCriteria(Criteria.where("car." + key).is(reqParams.get(key)));
                }

            });
            }

        returnPosts = mongoTemplate.find(query, Post.class);

        if(returnPosts.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Posts not found");
        }
        returnPosts.forEach(post -> post.setComments(post.getComments().stream().sorted(Comparator.comparing(Comment::getCreatedDate)).collect(Collectors.toList())));
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
        post.setComments(new ArrayList<>());
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
        Post post = postsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        post.setState(PostState.CLOSED);
        postsRepository.save(post);
        model.put("message", "Post " + id + " set resolved.");
        return ok(model);
    }

    public Object addComment(String token, String postId, NewMessage comment) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = userService.getUserByToken(token);
        Post post = postsRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        Comment newComment = new Comment(new ObjectId().toString(), comment.getContent(),currentUser.getId(),new Date(), new ArrayList<>(),new ArrayList<>());
        if(Objects.isNull(post.getComments())){
            post.setComments(new ArrayList<>());
        }
        post.getComments().add(newComment);
        postsRepository.save(post);
        model.put("message", "Add  " + comment + " to "+ postId + " post.");
        return ok(model);
    }

    public Object deleteComment(String postId, String id) {
        Map<Object, Object> model = new HashMap<>();
        Post post = postsRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
       post.getComments().removeIf(comment -> comment.getId().equals(id));
        postsRepository.save(post);
        model.put("message", "Comment from " + postId + " post deleted.");
        return ok(model);
    }

    public Object approveComment(String token, String postId, String id) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = userService.getUserByToken(token);
        Post post = postsRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        post.getComments().forEach(comment->{
            if(comment.getId().equals(id)){
                if(!isUserAlreadyAtList(currentUser.getId(), comment.getApproved())){
                    comment.getApproved().add(currentUser.getId());
                    model.put("message", "Approved comment " + id + " by " + currentUser.getId());
                }else{
                    model.put("message", "User ia already evaluate comment " + id);
                }
            }
        });
        postsRepository.save(post);
        return ok(model);
    }

    public Object rejectComment(String token, String postId, String id) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = userService.getUserByToken(token);
        Post post = postsRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
            post.getComments().forEach(comment -> {
                if (comment.getId().equals(id)) {
                    if(!isUserAlreadyAtList(currentUser.getId(), comment.getRejected())){
                        comment.getRejected().add(currentUser.getId());
                        model.put("message", "Rejected comment " + id + " by " + currentUser.getId());
                    }else{
                        model.put("message", "User ia already evaluate comment " + id);
                    }
                }
            });
            postsRepository.save(post);
        return ok(model);
    }

    private boolean isUserAlreadyAtList(String id, List<String> list){
        return list.stream().anyMatch(item->item.equals(id));
    }
}
