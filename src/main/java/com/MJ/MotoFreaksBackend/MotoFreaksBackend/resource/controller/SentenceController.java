package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewSentence;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.security.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sentence")
public class SentenceController {

    private final SentenceService sentenceService;

    @Autowired
    public SentenceController(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @RequestMapping(path = "/get/all", method = RequestMethod.GET, produces = "application/json")
    public Object getAllSentence(){
        return sentenceService.getAllSorted();
    }

    @RequestMapping(path = "/modify/add", method = RequestMethod.POST, produces = "application/json")
    public Object addSentence(HttpServletRequest req, @RequestBody NewSentence newSentence){
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return sentenceService.createNewTranslation(token,newSentence);
    }
    @RequestMapping(path = "/modify/merge/{id}", method = RequestMethod.POST, produces = "application/json")
    public Object addSentence(HttpServletRequest req, @RequestBody NewSentence newSentence, @PathVariable String id){
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return sentenceService.merge(token,id,newSentence);
    }
    @RequestMapping(path = "/modify/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteSentence(@PathVariable String id){
        return sentenceService.delete(id);
    }
}
