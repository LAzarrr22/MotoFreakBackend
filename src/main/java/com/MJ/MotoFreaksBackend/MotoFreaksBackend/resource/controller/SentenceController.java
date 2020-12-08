package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewSentence;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sentence")
@CrossOrigin("*")
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

    @RequestMapping(path = "/modify/merge", method = RequestMethod.POST, produces = "application/json")
    public Object mergeSentence(HttpServletRequest req, @RequestBody NewSentence newSentence){
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return sentenceService.merge(token,newSentence);
    }
    @RequestMapping(path = "/modify/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteSentence(@PathVariable String id){
        return sentenceService.delete(id);
    }
}
