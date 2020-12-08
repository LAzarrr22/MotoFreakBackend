package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewChallengeModel;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/challenge")
@CrossOrigin("*")
public class ChallengeController {

    private final ChallengeService challengeService;

    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @RequestMapping(path = "", method = RequestMethod.PUT, produces = "application/json")
    public Object createChallenge(HttpServletRequest req, @RequestBody NewChallengeModel challenge) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return challengeService.createChallenge(token, challenge);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.POST, produces = "application/json")
    public Object mergeChallenge(@PathVariable String id, @RequestBody NewChallengeModel challenge) {
        return challengeService.mergeChallenge(id, challenge);
    }

  @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteChallenge(@PathVariable String id) {
        return challengeService.deleteChallenge(id);
    }

    @RequestMapping(path = "/{challengeId}/competitor/points/{points}", method = RequestMethod.POST, produces = "application/json")
    public Object addCompetitor(HttpServletRequest req, @PathVariable String challengeId, @PathVariable int points) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return challengeService.addCompetitor(token, challengeId, points);
    }

    @RequestMapping(path = "", method = RequestMethod.GET, produces = "application/json")
    public Object findAll(HttpServletRequest req, @RequestParam Map<String, String> reqParams) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return challengeService.getAll(token,false,reqParams);
    }

    @RequestMapping(path = "/general", method = RequestMethod.GET, produces = "application/json")
    public Object findAllGeneral(HttpServletRequest req, @RequestParam Map<String, String> reqParams) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return challengeService.getAll(token,true, reqParams);
    }

    @RequestMapping(path = "/byUser/{id}", method = RequestMethod.GET, produces = "application/json")
    public Object findByUser(HttpServletRequest req, @PathVariable String id) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");

        return challengeService.findByUser(id, token);
    }

    @RequestMapping(path = "/{challengeId}/questions", method = RequestMethod.GET, produces = "application/json")
    public Object getQuestionsById(@PathVariable String challengeId) {
        return challengeService.getQuestions(challengeId);
    }

    @RequestMapping(path = "/exists/{name}", method = RequestMethod.GET, produces = "application/json")
    public Object isExists(@PathVariable String name) {
        return challengeService.isExistByName(name);
    }
}
