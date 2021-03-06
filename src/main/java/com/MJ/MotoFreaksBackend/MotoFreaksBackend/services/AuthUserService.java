package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.UserRoles;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Role;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Contact;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.UserRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.config.auth.JwtTokenProvider;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.AuthBody;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.RegisterBody;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response.LoginModelDto;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.RoleService;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class AuthUserService {
    @Autowired
    private UserRepository userRepository;

    @Qualifier("authenticationManagerBean")
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public Object loginUser(AuthBody data) {
        String username = data.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
        User currentUser = userService.getUserByUserName(username);
        String token = jwtTokenProvider.createToken(username, currentUser.getUserRoles());
        currentUser.getLoginsHistory().add(new Date());
        userRepository.save(currentUser);
        List<Role> roles = new ArrayList<>();
        currentUser.getUserRoles().forEach(userRoles -> roles.add(userRoles.getRole()));

        LoginModelDto loginSuccess = new LoginModelDto(username, checkValidateUser(currentUser), AuthorizationHeader.TOKEN_PREFIX + token, roles);
        log.info("User " + currentUser.getId() + " was logged correctly.");
        return ok(loginSuccess);
    }

    private boolean checkValidateUser(User currentUser) {
        boolean validation = true;
        if (currentUser.getAddress() == null ||currentUser.getGender() == null || currentUser.getCarsList().isEmpty()) {
            validation = false;
        }
        return validation;
    }

    public Object registerUser(RegisterBody data, Role role) {
        Map<Object, Object> model = new HashMap<>();
        try {
            userService.getUserByUserName(data.getUsername());
            log.warn("Cannot register user: " + data.getUsername() + ". User is already exists");
            return new ResponseEntity<Object>("User  '" + data.getUsername() + "' is already exists!", HttpStatus.NOT_FOUND);
        } catch (ResponseStatusException e) {
            saveNewUser(data, role);
            model.put("message", "User registered successfully");
            log.info("User " + data.getUsername() + " was register correctly.");
            return ok(model);
        }
    }

    public void saveNewUser(RegisterBody user, Role role) {
        User newUser = new User();
        newUser.setUserName(user.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setEnabled(true);
        newUser.setName(user.getName());
        newUser.setLastName(user.getLastName());
        newUser.setCreatedDate(new Date());
        newUser.setPoints(0);
        newUser.setFriendsList(new ArrayList<>());
        newUser.setMessages(new HashMap<>());
        newUser.setLoginsHistory(new ArrayList<>());
        newUser.setCarsList(new ArrayList<>());
        UserRoles userUserRoles = roleService.getRoleByName(role);
        newUser.setUserRoles(new HashSet<>(Collections.singletonList(userUserRoles)));
        newUser.setContact(new Contact(user.getEmail()));
        newUser.setAddress(new Address());
        userRepository.save(newUser);
    }

    public Object addRole(String id, Role role) {
        Map<Object, Object> model = new HashMap<>();
        User userExists = userService.getUserById(id);
        model.put("ID:", id);
        if (role == Role.ADMIN) {
            userExists.getUserRoles().add(this.roleService.getRoleByName(Role.MODERATOR));
            model.put("newRole", Role.MODERATOR.toString());
            log.info("Added " + Role.MODERATOR + " role to " + id + " user.");
        }
        userExists.getUserRoles().add(this.roleService.getRoleByName(role));
        model.put("newRole", role);
        this.userRepository.save(userExists);
        log.info("Added " + role + " role to " + id + " user.");
        return ok(model);
    }

    public Object getRoles(String token) {
        User currentUser = userService.getUserByToken(token);
        List<Role> roles = new ArrayList<>();
        currentUser.getUserRoles().forEach(userRoles -> roles.add(userRoles.getRole()));
        return ok(roles);
    }

    public Object checkUser(String token) {
        User currentUser = userService.getUserByToken(token);
        return ok(checkValidateUser(currentUser));
    }
}
