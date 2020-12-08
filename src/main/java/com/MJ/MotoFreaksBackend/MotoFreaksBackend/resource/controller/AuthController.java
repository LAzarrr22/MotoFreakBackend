package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Role;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.AuthBody;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.RegisterBody;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@Slf4j
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserService customUserAuthService;

    @Autowired
    public AuthController(AuthUserService customUserAuthService) {
        this.customUserAuthService = customUserAuthService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, produces = "application/json")
    public Object login(@RequestBody AuthBody data) {
        return customUserAuthService.loginUser(data);
    }


    @RequestMapping(path = "/register", method = RequestMethod.PUT, produces = "application/json")
    public Object register(@RequestBody RegisterBody user) {
        return customUserAuthService.registerUser(user, Role.USER);
    }

    @RequestMapping(path = "/validation", method = RequestMethod.GET, produces = "application/json")
    public Object checkValidationUser(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return customUserAuthService.checkUser(token);
    }

    @RequestMapping(path = "/set/moderator/{id}", method = RequestMethod.POST, produces = "application/json")
    public Object addModeratorRole(@PathVariable String id) {
        return customUserAuthService.addRole(id, Role.MODERATOR);
    }

    @RequestMapping(path = "/set/admin/{id}", method = RequestMethod.POST, produces = "application/json")
    public Object addAdminRole(@PathVariable String id) {
        return customUserAuthService.addRole(id, Role.ADMIN);
    }

    @RequestMapping(path = "/roles", method = RequestMethod.GET, produces = "application/json")
    public Object getRoles(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return customUserAuthService.getRoles(token);
    }
}
