package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Challenge;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.ChallengeRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewChallengeModel;
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
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Object createChallenge(String token, NewChallengeModel challenge) {
        Map<Object, Object> model = new HashMap<>();
        if (!isExistByName(challenge.getName())) {
            Challenge newChallenge = new Challenge();
            newChallenge.setCreatedDate(new Date());
            newChallenge.setCreatorUserName(userService.getUserByToken(token).getUserName());
            newChallenge.setName(challenge.getName());
            newChallenge.setCompany(challenge.getCompany());
            newChallenge.setModel(challenge.getModel());
            newChallenge.setGeneration(challenge.getGeneration());
            newChallenge.setGroupId("test");//todo
            newChallenge.setQAList(challenge.getQAList());
            challengeRepository.save(newChallenge);
            model.put("message", "Challenge " + challenge.getName() + " was created.");
            log.info("Challenge " + challenge.getName() + " was created by " + newChallenge.getCreatorUserName());
        }
        return ok(model);
    }

    public Object findByCar(Map<String, String> carParam) {
        Query query = new Query();
        carParam.keySet().forEach(key -> {
            query.addCriteria(Criteria.where(key).is(carParam.get(key)));
        });
        List<Challenge> challengeList = mongoTemplate.find(query, Challenge.class);
        if (challengeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found");
        }
        return challengeList;
    }

    public Object findByUser(String username) {
        Optional<Challenge> optionalChallenge = challengeRepository.findByUsername(username);
        return optionalChallenge.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found"));
    }

    public Object findById(String id) {
        Optional<Challenge> optionalChallenge = challengeRepository.findById(id);
        return optionalChallenge.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found"));
    }

    public boolean isExistByName(String name) {
        Optional<Challenge> optionalChallenge = challengeRepository.findByName(name);
        if (optionalChallenge.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Challenge with that name is already exists");
        return false;
    }
}