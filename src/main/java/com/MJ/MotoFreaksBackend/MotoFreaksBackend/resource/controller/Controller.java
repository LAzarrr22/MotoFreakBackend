package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface Controller {

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    void delete(@PathVariable("id") String id);

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = "application/json")
    List getAll();
}
