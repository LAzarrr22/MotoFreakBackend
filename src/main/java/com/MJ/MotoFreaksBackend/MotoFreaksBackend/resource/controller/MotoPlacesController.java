package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.MotoPlaces;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.RecomencdationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendation")
public class MotoPlacesController implements Controller {

    private final RecomencdationRepository recomencdationRepository;

    public MotoPlacesController(RecomencdationRepository recomencdationRepository) {
        this.recomencdationRepository = recomencdationRepository;
    }

    @Override
    public void delete(String id) {
        this.recomencdationRepository.deleteById(id);
    }

    @Override
    public List<MotoPlaces> getAll() {
        return recomencdationRepository.findAll();
    }


    @PutMapping
    public void insert(@RequestBody MotoPlaces motoPlaces) {
        this.recomencdationRepository.insert(motoPlaces);
    }

    @PostMapping
    public void update(@RequestBody MotoPlaces motoPlaces) {
        this.recomencdationRepository.save(motoPlaces);
    }
}
