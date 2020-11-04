package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewPost;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@Slf4j
public class PostsController {

    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @RequestMapping(path = "/get/ALL", method = RequestMethod.GET, produces = "application/json")
    public Object getAll(@RequestParam Map<String, String> carParam) {
        return postsService.getPosts(null,carParam,null);
    }

    @RequestMapping(path = "/get/{type}", method = RequestMethod.GET, produces = "application/json")
    public Object getAllByType(@PathVariable PostType type, @RequestParam Map<String, String> carParam) {
        return postsService.getPosts(type, carParam,null);
    }
    @RequestMapping(path = "/creator/id/{id}/get/ALL", method = RequestMethod.GET, produces = "application/json")
    public Object getPostsById(@PathVariable String id, @RequestParam Map<String, String> carParam) {
        return postsService.getPosts(null, carParam, id);
    }
    @RequestMapping(path = "/creator/id/{id}/get/{type}", method = RequestMethod.GET, produces = "application/json")
    public Object getPostsByIAndType(@PathVariable String id, @PathVariable PostType type, @RequestParam Map<String, String> carParam) {
        return postsService.getPosts(type, carParam, id);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = "application/json")
    public Object addPost(HttpServletRequest req, @RequestBody NewPost newPost) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return postsService.addPost(newPost, token);
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object getMyPosts(@PathVariable String id) {
        return postsService.deletePost(id);
    }
}
