package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Contact;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.MergeUser;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewCar;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = "application/json")
    public Object getAll(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.getAllUsers(token);
    }


    @RequestMapping(path = "/merge", method = RequestMethod.POST, produces = "application/json")
    public Object mergeUser(HttpServletRequest req, @RequestBody MergeUser user) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.mergeUser(token, user);
    }

    @RequestMapping(path = "/merge/address", method = RequestMethod.POST, produces = "application/json")
    public Object addAddress(HttpServletRequest req, @RequestBody Address address) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.mergeAddress(token, address);
    }

    @RequestMapping(path = "/merge/contact", method = RequestMethod.POST, produces = "application/json")
    public Object addContact(HttpServletRequest req, @RequestBody Contact contact) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.mergeContact(token, contact);
    }

    @RequestMapping(path = "/add/car", method = RequestMethod.POST, produces = "application/json")
    public Object addCar(HttpServletRequest req, @RequestBody NewCar car) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.addCar(token, car);
    }

    @RequestMapping(path = "/remove/car/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object addCar(HttpServletRequest req, @PathVariable String id) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.removeCar(token, id);
    }

    @RequestMapping(path = "/merge/car/{carId}", method = RequestMethod.POST, produces = "application/json")
    public Object mergeCar(HttpServletRequest req, @RequestBody NewCar car, @PathVariable String carId) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.mergeCar(token, car, carId);
    }

    @RequestMapping(path = "/add/friend/{friendId}", method = RequestMethod.POST, produces = "application/json")
    public Object addFriend(HttpServletRequest req, @PathVariable String friendId) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.addFriend(token, friendId);
    }

    @RequestMapping(path = "/get/friends", method = RequestMethod.GET, produces = "application/json")
    public Object getFriends(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.getFriends(token);
    }

    @RequestMapping(path = "/show/profile/{id}", method = RequestMethod.GET, produces = "application/json")
    public Object showProfile(@PathVariable String id, HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.getProfile(id, token);
    }

    @RequestMapping(path = "/show/profile", method = RequestMethod.GET, produces = "application/json")
    public Object showMyProfile(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.getMyProfile(token);
    }

    @RequestMapping(path = "/add/points/{value}", method = RequestMethod.POST, produces = "application/json")
    public Object addPoints(HttpServletRequest req, @PathVariable int value) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.addPoints(token, value);
    }

    @RequestMapping(path = "/remove/points/{value}", method = RequestMethod.POST, produces = "application/json")
    public Object removePoints(HttpServletRequest req, @PathVariable int value) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return userService.removePoints(token, value);
    }

}

