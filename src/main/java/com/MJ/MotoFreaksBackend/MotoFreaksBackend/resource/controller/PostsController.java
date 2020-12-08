package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.PostType;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewMessage;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewPost;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@Slf4j
@CrossOrigin("*")
public class PostsController {

    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @RequestMapping(path = "/get/ALL", method = RequestMethod.GET, produces = "application/json")
    public Object getAll(@RequestParam Map<String, String> reqParams) {
        return postsService.getPosts(null,reqParams,null);
    }

    @RequestMapping(path = "/get/{type}", method = RequestMethod.GET, produces = "application/json")
    public Object getAllByType(@PathVariable PostType type, @RequestParam Map<String, String> reqParams) {
        return postsService.getPosts(type, reqParams,null);
    }
    @RequestMapping(path = "/creator/id/{id}/get/ALL", method = RequestMethod.GET, produces = "application/json")
    public Object getPostsById(@PathVariable String id, @RequestParam Map<String, String> reqParams) {
        return postsService.getPosts(null, reqParams, id);
    }
    @RequestMapping(path = "/creator/id/{id}/get/{type}", method = RequestMethod.GET, produces = "application/json")
    public Object getPostsByIAndType(@PathVariable String id, @PathVariable PostType type, @RequestParam Map<String, String> reqParams) {
        return postsService.getPosts(type, reqParams, id);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = "application/json")
    public Object addPost(HttpServletRequest req, @RequestBody NewPost newPost) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return postsService.addPost(newPost, token);
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteMyPost(@PathVariable String id) {
        return postsService.deletePost(id);
    }

    @RequestMapping(path = "/resolve/{id}", method = RequestMethod.POST, produces = "application/json")
    public Object resolveMyPosts(@PathVariable String id) {
        return postsService.resolvePost(id);
    }

    @RequestMapping(path = "/{postId}/add/comment", method = RequestMethod.POST, produces = "application/json")
    public Object addComment(HttpServletRequest req, @PathVariable String postId, @RequestBody NewMessage newComment) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return postsService.addComment(token, postId, newComment);
    }

    @RequestMapping(path = "/{postId}/delete/comment/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteComment(@PathVariable String postId,  @PathVariable String id) {
        return postsService.deleteComment(postId, id);
    }

    @RequestMapping(path = "/{postId}/approve/comment/{id}", method = RequestMethod.POST, produces = "application/json")
    public Object approveComment(HttpServletRequest req, @PathVariable String postId,  @PathVariable String id) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return postsService.approveComment(token,postId, id);
    }

    @RequestMapping(path = "/{postId}/reject/comment/{id}", method = RequestMethod.POST, produces = "application/json")
    public Object rejectComment(HttpServletRequest req, @PathVariable String postId,  @PathVariable String id) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return postsService.rejectComment(token,postId, id);
    }
}
