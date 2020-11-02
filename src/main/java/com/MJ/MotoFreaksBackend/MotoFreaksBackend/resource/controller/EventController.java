package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Event;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController implements Controller {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void delete(String id) {
        this.eventRepository.deleteById(id);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }


    @PutMapping
    public void insert(@RequestBody Event event) {
        this.eventRepository.insert(event);
    }

    @PostMapping
    public void update(@RequestBody Event event) {
        this.eventRepository.save(event);
    }
}
