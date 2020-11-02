package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Challenge;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.QuestionAnswer;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.ChallengeRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewChallengeModel;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.response.ChallengeDto;
import lombok.extern.slf4j.Slf4j;
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
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository, UserService userService, MongoTemplate mongoTemplate) {
        this.challengeRepository = challengeRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }


    public Object createChallenge(String token, NewChallengeModel challenge) {
        Map<Object, Object> model = new HashMap<>();
        if (!isExistByName(challenge.getName())) {
            Challenge newChallenge = new Challenge();
            newChallenge.setCreatedDate(new Date());
            newChallenge.setCreatorId(userService.getUserByToken(token).getId());
            newChallenge.setName(challenge.getName());
            newChallenge.setCompany(challenge.getCompany());
            newChallenge.setModel(challenge.getModel());
            newChallenge.setGeneration(challenge.getGeneration());
            newChallenge.setQaList(challenge.getQaList());
            newChallenge.setCompetitorIdList(new ArrayList<>());
            challengeRepository.save(newChallenge);
            model.put("message", "Challenge " + challenge.getName() + " was created.");
            log.info("Challenge " + challenge.getName() + " was created by " + newChallenge.getCreatorId());
        }
        return ok(model);
    }

    public Object addCompetitor(String token, String id) {
        Map<Object, Object> model = new HashMap<>();
        String userId = userService.getUserByToken(token).getId();
        Challenge foundChallenge = findById(id);
        foundChallenge.getCompetitorIdList().add(userId);
        challengeRepository.save(foundChallenge);
        model.put("message", "Added competitor to " + foundChallenge.getName() + " challenge");
        log.info("Added competitor " + userId + " to " + foundChallenge.getName() + " challenge ");
        return ok(model);
    }

    public Object findByCar(Map<String, String> carParam, String token) {
        String userId = userService.getUserByToken(token).getId();
        Query query = new Query();
        carParam.keySet().forEach(key -> {
            query.addCriteria(Criteria.where(key).is(carParam.get(key)));
        });
        List<Challenge> challengeList = mongoTemplate.find(query, Challenge.class);
        if (challengeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found");
        }
        return sortByName(mappingToDtoChallenges(challengeList, userId), true);
    }

    public Object findByUser(String id, String token) {
        String userId = userService.getUserByToken(token).getId();
        List<Challenge> findChallengeList = challengeRepository.findAll().stream().filter(challenge -> challenge.getCreatorId().equals(id)).collect(Collectors.toList());
        return findChallengeList.isEmpty() ? new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found") : mappingToDtoChallenges(findChallengeList, userId);
    }

    public Challenge findById(String id) {
        Optional<Challenge> optionalChallenge = challengeRepository.findById(id);
        return optionalChallenge.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found"));
    }

    public boolean isExistByName(String name) {
        Optional<Challenge> optionalChallenge = challengeRepository.findByName(name);
        if (optionalChallenge.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Challenge with that name is already exists");
        return false;
    }

    public Object getAll(String token) {
        String userId = userService.getUserByToken(token).getId();
        List<Challenge> challengeList = challengeRepository.findAll();
        return sortByName(mappingToDtoChallenges(challengeList, userId), true);
    }

    private List<ChallengeDto> sortByName(List<ChallengeDto> mixList, boolean direction) {
        if (direction) {
            return mixList.stream().sorted(Comparator.comparing(ChallengeDto::getName)).collect(Collectors.toList());
        }
        return mixList.stream().sorted(Comparator.comparing(ChallengeDto::getName).reversed()).collect(Collectors.toList());

    }

    private List<ChallengeDto> mappingToDtoChallenges(List<Challenge> challengeList, String userId) {
        List<ChallengeDto> challengeDtoList = new ArrayList<>();
        challengeList.forEach(challenge -> {
            challengeDtoList.add(new ChallengeDto(challenge.getId(), challenge.getName(), challenge.getCompany(), challenge.getModel()
                    , challenge.getGeneration(), challenge.getCreatorId(), isAlreadyFilled(userId, challenge), challenge.getQaList().size(), countAllPoints(challenge.getQaList())));
        });
        return challengeDtoList;
    }

    private int countAllPoints(List<QuestionAnswer> questionList) {
        return questionList.stream().mapToInt(QuestionAnswer::getPoints).sum();
    }

    public Object getQuestions(String id) {
        Optional<Challenge> optionalChallenge = challengeRepository.findById(id);
        return optionalChallenge.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found")).getQaList();
    }

    private boolean isAlreadyFilled(String userId, Challenge challenge) {
        String commonFriends = challenge.getCompetitorIdList().stream().filter(userId::equals).findAny().orElse("");
        return !commonFriends.isEmpty();
    }

}
