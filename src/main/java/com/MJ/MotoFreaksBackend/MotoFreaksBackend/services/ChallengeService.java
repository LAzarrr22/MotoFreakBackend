package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Challenge;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.ChallengeStateForUser;
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
            newChallenge.setCompetitorIdList(new HashMap<>());
            mapRequestToDB(challenge, newChallenge);
            model.put("message", "Challenge " + challenge.getName() + " was created.");
            log.info("Challenge " + challenge.getName() + " was created by " + newChallenge.getCreatorId());
        }
        return ok(model);
    }

    public Object mergeChallenge(String challengeId, NewChallengeModel challenge) {
        Challenge existsChallenge = findById(challengeId);
        Map<Object, Object> model = new HashMap<>();
        existsChallenge.setUpdatedDate(new Date());
        mapRequestToDB(challenge, existsChallenge);
        model.put("message", "Challenge " + challenge.getName() + " was merged.");
        log.info("Challenge " + challenge.getName() + " was merged.");

        return ok(model);
    }

    private void mapRequestToDB(NewChallengeModel requestChallenge, Challenge mergeChallenge) {
        mergeChallenge.setName(requestChallenge.getName());
        mergeChallenge.setCompany(requestChallenge.getCompany());
        mergeChallenge.setModel(requestChallenge.getModel());
        mergeChallenge.setGeneration(requestChallenge.getGeneration());
        mergeChallenge.setGeneral(requestChallenge.isGeneral());
        mergeChallenge.setQaList(requestChallenge.getQaList());
        challengeRepository.save(mergeChallenge);
    }

    public Object deleteChallenge(String id) {
        Map<Object, Object> model = new HashMap<>();
        challengeRepository.deleteById(id);
        model.put("message", "Challenge " + id + " was removed");
        return ok(model);
    }

    public Object addCompetitor(String token, String id, int obtainPoints) {
        Map<Object, Object> model = new HashMap<>();
        String userId = userService.getUserByToken(token).getId();
        Challenge foundChallenge = findById(id);
        foundChallenge.getCompetitorIdList().put(userId, obtainPoints);
        challengeRepository.save(foundChallenge);
        model.put("message", "Added competitor to " + foundChallenge.getName() + " challenge");
        log.info("Added competitor " + userId + " to " + foundChallenge.getName() + " challenge ");
        return ok(model);
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
        return optionalChallenge.isPresent();
    }

    public Object getAll(String token, Boolean isGeneral, Map<String, String> reqParams) {
        String userId = userService.getUserByToken(token).getId();
        return sortByName(mappingToDtoChallenges(filterParams(reqParams, isGeneral, userId), userId), true);
    }

    private List<Challenge> filterParams(Map<String, String> reqParams, boolean isGeneral, String userId) {
        Query query = new Query();
        List<Challenge> findChallenges;
        String isFilledState = "";
        if (!reqParams.isEmpty() && reqParams.get("state") != null) {
            isFilledState = reqParams.get("state");
            reqParams.remove("state");
        }
        if (isGeneral) {
            findChallenges = getAllGeneral();
        } else {
            if (!reqParams.isEmpty()) {

                reqParams.keySet().forEach(key -> {
                    log.error(key);
                    if (key.equals("company") || key.equals("model") || key.equals("generation")) {
                        query.addCriteria(Criteria.where(key).is(reqParams.get(key)));
                    }
                });
            }
            findChallenges = mongoTemplate.find(query, Challenge.class);
        }

        if (isFilledState.equals(ChallengeStateForUser.FILLED.toString())) {
            findChallenges = filterFilled(findChallenges, userId, true);
        } else if (isFilledState.equals(ChallengeStateForUser.UNFILLED.toString())) {
            findChallenges = filterFilled(findChallenges, userId, false);
        }
        if (findChallenges.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Posts not found");
        }
        return findChallenges;
    }

    public List<Challenge> getAllGeneral() {
        return challengeRepository.findAll().stream().filter(Challenge::isGeneral).collect(Collectors.toList());
    }

    private List<Challenge> filterFilled(List<Challenge> challenges, String userId, boolean isFilled) {
        List<Challenge> findChallenges = new ArrayList<>();
        challenges.forEach(challenge -> {
            if (isFilled) {
                if (isAlreadyFilled(userId, challenge)) {
                    findChallenges.add(challenge);
                }
            } else {
                if (!isAlreadyFilled(userId, challenge)) {
                    findChallenges.add(challenge);
                }
            }
        });
        return findChallenges;
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
                    , challenge.getGeneration(), challenge.getCreatorId(), challenge.isGeneral(), isAlreadyFilled(userId, challenge), obtainPoints(userId, challenge), challenge.getQaList().size(), countAllPoints(challenge.getQaList())));
        });
        return challengeDtoList;
    }

    private int obtainPoints(String userId, Challenge challenge) {
        if (isAlreadyFilled(userId, challenge)) {
            return challenge.getCompetitorIdList().get(userId);
        }
        return 0;
    }

    private int countAllPoints(List<QuestionAnswer> questionList) {
        return questionList.stream().mapToInt(QuestionAnswer::getPoints).sum();
    }

    public Object getQuestions(String id) {
        Optional<Challenge> optionalChallenge = challengeRepository.findById(id);
        return optionalChallenge.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found")).getQaList();
    }

    private boolean isAlreadyFilled(String userId, Challenge challenge) {
        String userFound = challenge.getCompetitorIdList().keySet().stream().filter(userId::equals).findAny().orElse("");
        return !userFound.isEmpty();
    }


}
