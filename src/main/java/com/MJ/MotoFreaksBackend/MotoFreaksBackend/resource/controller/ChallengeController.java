package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewChallengeModel;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/challenge")
@CrossOrigin(origins = "http://localhost:4200")
public class ChallengeController {

    private final ChallengeService challengeService;

    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST, produces = "application/json")
    public Object createChallenge(HttpServletRequest req, @RequestBody NewChallengeModel challenge) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return challengeService.createChallenge(token, challenge);
    }

    @RequestMapping(path = "/id/{id}/add/competitor", method = RequestMethod.POST, produces = "application/json")
    public Object addCompetitor(HttpServletRequest req, @PathVariable String id) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return challengeService.addCompetitor(token, id);
    }

    @RequestMapping(path = "/get/all", method = RequestMethod.GET, produces = "application/json")
    public Object findAll(HttpServletRequest req) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");

        return challengeService.getAll(token);
    }

    @RequestMapping(path = "/get/findBy/car", method = RequestMethod.GET, produces = "application/json")
    public Object findByCar(HttpServletRequest req, @RequestParam Map<String, String> carParam) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return challengeService.findByCar(carParam, token);
    }

    @RequestMapping(path = "/get/findBy/user/id/{id}", method = RequestMethod.GET, produces = "application/json")
    public Object findByUser(HttpServletRequest req, @PathVariable String id) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");

        return challengeService.findByUser(id, token);
    }

    @RequestMapping(path = "/get/questions/id/{id}", method = RequestMethod.GET, produces = "application/json")
    public Object getQuestionsById(@PathVariable String id) {
        return challengeService.getQuestions(id);
    }

    @RequestMapping(path = "/get/check/exists/{name}", method = RequestMethod.GET, produces = "application/json")
    public Object isExists(@PathVariable String name) {
        return challengeService.isExistByName(name);
    }
}
