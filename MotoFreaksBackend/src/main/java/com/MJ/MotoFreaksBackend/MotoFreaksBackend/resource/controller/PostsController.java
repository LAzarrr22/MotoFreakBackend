package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewPost;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/posts")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class PostsController {

    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @RequestMapping(path = "/get/ALL", method = RequestMethod.GET, produces = "application/json")
    public Object getAll() {
        return postsService.getAll();
    }

    @RequestMapping(path = "/get/{type}", method = RequestMethod.GET, produces = "application/json")
    public Object getAllByType(@PathVariable PostType type) {
        return postsService.getAllByType(type);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = "application/json")
    public Object addPost(HttpServletRequest req, @RequestBody NewPost newPost) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return postsService.addPost(newPost, token);
    }

    @RequestMapping(path = "/my-posts", method = RequestMethod.GET, produces = "application/json")
    public Object getMyPosts(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return postsService.getMyPosts(token);
    }

    @RequestMapping(path = "/all/id/{id}", method = RequestMethod.GET, produces = "application/json")
    public Object getPostsById(HttpServletRequest req, @PathVariable String id) {
        return postsService.getPostsById(id);
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object getMyPosts(@PathVariable String id) {
        return postsService.deletePost(id);
    }
}
