package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Sentence;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.SentenceRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewSentence;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SentenceService {

    private final SentenceRepository sentenceRepository;
    private final UserService userService;

    @Autowired
    public SentenceService(SentenceRepository sentenceRepository, UserService userService) {
        this.sentenceRepository = sentenceRepository;
        this.userService = userService;
    }

    public Object getAllSorted() {
        List<Sentence> allSentence = sentenceRepository.findAll();
    return allSentence.stream().sorted(Comparator.comparing(Sentence::getName));
    }

    private Object createNewTranslation(String userId, NewSentence newSentence){
        Sentence sentenceToCreate = new Sentence();
        sentenceToCreate.setCreatedDate(new Date());
        sentenceToCreate.setCreatorId(userId);
        sentenceToCreate.setName(newSentence.getName());
        sentenceToCreate.setTranslation(newSentence.getTranslation());
        sentenceRepository.save(sentenceToCreate);
        log.info("New sentence "+newSentence.getName()+" was created");
        return getAllSorted();
    }

    private Object updateExistsTranslation(String userId, NewSentence newSentence){
        Sentence sentenceToMerge= sentenceRepository.findById(newSentence.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sentence not found"));

        sentenceToMerge.setUpdatedDate(new Date());
        sentenceToMerge.setUpdatedById(userId);
        sentenceToMerge.setName(newSentence.getName());
        sentenceToMerge.setTranslation(newSentence.getTranslation());
        sentenceRepository.save(sentenceToMerge);
        return getAllSorted();
    }

    public Object merge(String token, NewSentence newSentence) {
        String userId = userService.getUserByToken(token).getId();
        if(Strings.isNullOrEmpty(newSentence.getId())){
           return createNewTranslation(userId, newSentence);
        }else{
           return updateExistsTranslation(userId,newSentence);
        }
    }

    public Object delete(String id) {
        sentenceRepository.deleteById(id);

        return getAllSorted();
    }
}
