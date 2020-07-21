package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource;


import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.Group;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.GroupRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupResource implements Resources {


    private final GroupRepository groupRepository;

    public GroupResource(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void delete(String id) {
        this.groupRepository.deleteById(id);
    }

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }


    @PutMapping
    public void insert(@RequestBody Group group) {
        this.groupRepository.insert(group);
    }

    @PostMapping
    public void update(@RequestBody Group group) {
        this.groupRepository.save(group);
    }
}
