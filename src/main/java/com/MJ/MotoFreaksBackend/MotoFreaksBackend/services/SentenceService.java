package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Sentence;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.SentenceRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewSentence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

    public Object createNewTranslation(String token, NewSentence newSentence){
        String userId = userService.getUserByToken(token).getId();
        Sentence sentenceToCreate = new Sentence();
        sentenceToCreate.setCreatedDate(new Date());
        sentenceToCreate.setCreatorId(userId);
        sentenceToCreate.setName(newSentence.getName());
        sentenceToCreate.setTranslation(newSentence.getTranslation());
        sentenceRepository.save(sentenceToCreate);
        log.info("New sentence "+newSentence.getName()+" was created");
        return getAllSorted();
    }

    public Object merge(String token, String id, NewSentence newSentence) {
        String userId = userService.getUserByToken(token).getId();
        Sentence sentenceToMerge= sentenceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sentence not found"));

        sentenceToMerge.setUpdatedDate(new Date());
        sentenceToMerge.setUpdatedById(userId);
        sentenceToMerge.setName(newSentence.getName());
        sentenceToMerge.setTranslation(newSentence.getTranslation());
        sentenceRepository.save(sentenceToMerge);

        return getAllSorted();
    }

    public Object delete(String id) {
        sentenceRepository.deleteById(id);

        return getAllSorted();
    }
}
