package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    String hello() {
        return "HELLO WORLD";
    }
}
