package ru.dan.eduinstitution.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SimpleController {

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }
}
