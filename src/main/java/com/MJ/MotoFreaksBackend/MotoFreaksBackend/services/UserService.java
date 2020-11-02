package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Role;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.CarDataModel;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Contact;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.UserRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.MergeUser;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewCar;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response.MyFriendDto;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response.MyUserDto;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response.UserDto;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.configs.JwtTokenProvider;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtService, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserByToken(String token) {
        Optional<User> optionalUser = userRepository.findByUserNameOptional(jwtService.getUsername(token));
        return optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User getUserByUserName(String username) {
        Optional<User> optionalUserFriend = userRepository.findByUserNameOptional(username);
        return optionalUserFriend.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User '" + username + "' not found"));
    }

    public User getUserById(String id) {
        Optional<User> optionalUserFriend = userRepository.findById(id);
        return optionalUserFriend.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }


    public Object addCar(String token, NewCar car) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        if (Objects.isNull(currentUser.getCarsList())) {
            currentUser.setCarsList(new ArrayList<>());
        }
        currentUser.getCarsList().add(new CarDataModel(new ObjectId().toString(), new Date(), null, car.getName(), car.getRegistration().toUpperCase(), car.getCompany()
                , car.getModel(), car.getGeneration(), car.getYear(), car.getColor(), car.getEngine(), car.getHorsepower(), car.getTorque()));
        currentUser.setUpdatedDate(new Date());
        userRepository.save(currentUser);
        model.put("message", "Car added successful.");
        log.info("Car " + car.getName() + " added to " + currentUser.getId() + " user.");
        return ok(model);
    }

    public Object removeCar(String token, String id) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        currentUser.getCarsList().removeIf(carDataModel -> carDataModel.getId().equals(id));
        userRepository.save(currentUser);
        model.put("message", "Car removed successful.");
        log.info("Car " + id + " removed from " + currentUser.getId() + " user.");
        return ok(model);
    }

    public Object mergeCar(String token, NewCar car, String carId) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        currentUser.getCarsList().stream().filter(selectedCar -> selectedCar.getId().equals(carId)).forEach(carToEdit -> {
            carToEdit.setUpdatedDate(new Date());
            carToEdit.setName(car.getName());
            carToEdit.setRegistration(car.getRegistration());
            carToEdit.setCompany(car.getCompany());
            carToEdit.setModel(car.getModel());
            carToEdit.setGeneration(car.getGeneration());
            carToEdit.setYear(car.getYear());
            carToEdit.setColor(car.getColor());
            carToEdit.setEngine(car.getEngine());
            carToEdit.setHorsepower(car.getHorsepower());
            carToEdit.setTorque(car.getTorque());
        });
        userRepository.save(currentUser);
        model.put("message", "Car merged successful.");
        log.info("Car " + car.getName() + " merged for " + currentUser.getId() + " user.");
        return ok(model);
    }

    public Object mergeUser(String token, MergeUser mergeUser) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        currentUser.setName(mergeUser.getName());
        currentUser.setLastName(mergeUser.getLastName());
        currentUser.setEnabled(mergeUser.isEnabled());
        currentUser.setGender(mergeUser.getGender());
        currentUser.setUpdatedDate(new Date());
        if (!Strings.isNullOrEmpty(mergeUser.getPassword())) {
            currentUser.setPassword(bCryptPasswordEncoder.encode(mergeUser.getPassword()));
        }
        userRepository.save(currentUser);
        model.put("message", "User " + currentUser.getUserName() + " was updated");
        log.info("User " + currentUser.getId() + " was updated");
        return ok(model);
    }


    public Object mergeAddress(String token, Address address) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        currentUser.setAddress(address);
        currentUser.setUpdatedDate(new Date());
        userRepository.save(currentUser);
        model.put("message", "Address was merged for " + currentUser.getUserName() + " user.");
        log.info("Address was merged for " + currentUser.getId() + " user.");
        return ok(model);
    }

    public Object mergeContact(String token, Contact contact) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        currentUser.setContact(contact);
        currentUser.setUpdatedDate(new Date());
        userRepository.save(currentUser);
        model.put("message", "Contacts was merged for " + currentUser.getUserName() + " user.");
        log.info("Contacts was merged for " + currentUser.getId() + " user.");
        return ok(model);
    }

    public Object addFriend(String token, String friendId) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        User userFriend = getUserById(friendId);
        if (isYourFriend(currentUser, friendId) || currentUser.getId().equals(friendId)) {
            model.put("message", "Cannot add " + userFriend.getUserName() + " to friends " + currentUser.getUserName() + " user.");
            log.warn("Cannot add " + userFriend.getId() + " to friends " + currentUser.getId() + " user.");
            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        } else {
            currentUser.getFriendsList().add(userFriend.getId());
            currentUser.setUpdatedDate(new Date());
        }
        userRepository.save(currentUser);
        model.put("message", "Friend " + userFriend.getUserName() + " added to " + currentUser.getUserName() + " user.");
        log.info("Friend " + userFriend.getId() + " added to " + currentUser.getId() + " user.");
        return ok(model);
    }

    public Object getFriends(String token) {
        User currentUser = getUserByToken(token);
        List<MyFriendDto> myFriendList = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            currentUser.getFriendsList().forEach(friendId -> {
                if (user.getId().equals(friendId))
                    myFriendList.add(new MyFriendDto(user.getId(), user.getName(), user.getLastName(), user.getGender()));
            });
        });
        return ok(myFriendList);
    }

    public Object getProfile(String id, String currentToken) {
        User currentUser = getUserByToken(currentToken);
        User userToShow = getUserById(id);
        List<Role> roles = new ArrayList<>();
        userToShow.getUserRoles().forEach(userRoles -> roles.add(userRoles.getRole()));
        UserDto profile = new UserDto(userToShow.getId(), userToShow.getName(), userToShow.getLastName(), userToShow.getGender(), userToShow.isEnabled()
                , userToShow.getCarsList(), userToShow.getContact(), userToShow.getAddress(),
                userToShow.getPoints(), userToShow.getFriendsList(), roles, isYourFriend(currentUser, userToShow.getUserName()));
        return ok(profile);
    }

    public Object getMyProfile(String token) {
        User currentUser = getUserByToken(token);
        MyUserDto myProfile
                = new MyUserDto(currentUser.getId(), currentUser.getUserName(), currentUser.getName(), currentUser.getLastName(), currentUser.getGender(), currentUser.isEnabled(),
                currentUser.getCreatedDate(), currentUser.getUpdatedDate(), currentUser.getLoginsHistory(), currentUser.getCarsList(),
                currentUser.getContact(), currentUser.getAddress(), currentUser.getPoints());

        return ok(myProfile);
    }

    public Object addPoints(String token, int points) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        currentUser.setPoints(currentUser.getPoints() + points);
        currentUser.setUpdatedDate(new Date());
        userRepository.save(currentUser);
        model.put("message", points + " points added to " + currentUser.getUserName() + " user.");
        log.info(points + " points added to " + currentUser.getId() + " user.");
        return ok(model);
    }

    public Object removePoints(String token, int points) {
        Map<Object, Object> model = new HashMap<>();
        User currentUser = getUserByToken(token);
        currentUser.setPoints(currentUser.getPoints() - points);
        currentUser.setUpdatedDate(new Date());
        userRepository.save(currentUser);
        model.put("message", points + " points removed from " + currentUser.getUserName() + " user.");
        log.info(points + " points points removed from " + currentUser.getId() + " user.");
        return ok(model);
    }

    private boolean isYourFriend(User currentUser, String id) {
        if (currentUser.getId().equals(id)) {
            return true;
        }
        String commonFriends = currentUser.getFriendsList().stream().filter(id::equals).findAny().orElse("");
        return !commonFriends.isEmpty();
    }

    public Object getAllUsers(String token) {
        User currentUser = getUserByToken(token);
        List<UserDto> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            List<Role> roles = new ArrayList<>();
            user.getUserRoles().forEach(userRoles -> roles.add(userRoles.getRole()));
            allUsers.add(new UserDto(user.getId(), user.getName(), user.getLastName(), user.getGender(), user.isEnabled(), user.getCarsList(),
                    user.getContact(), user.getAddress(), user.getPoints(), user.getFriendsList(), roles, isYourFriend(currentUser, user.getId()))
            );
        });
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }


}
