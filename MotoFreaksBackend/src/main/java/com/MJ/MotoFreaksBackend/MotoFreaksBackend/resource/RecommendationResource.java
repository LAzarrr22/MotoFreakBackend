package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Recommendation;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.RecomencdationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendation")
public class RecommendationResource implements Resources {

    private final RecomencdationRepository recomencdationRepository;

    public RecommendationResource(RecomencdationRepository recomencdationRepository) {
        this.recomencdationRepository = recomencdationRepository;
    }

    @Override
    public void delete(String id) {
        this.recomencdationRepository.deleteById(id);
    }

    @Override
    public List<Recommendation> getAll() {
        return recomencdationRepository.findAll();
    }


    @PutMapping
    public void insert(@RequestBody Recommendation recommendation) {
        this.recomencdationRepository.insert(recommendation);
    }

    @PostMapping
    public void update(@RequestBody Recommendation recommendation) {
        this.recomencdationRepository.save(recommendation);
    }
}
