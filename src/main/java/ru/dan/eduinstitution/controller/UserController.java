package ru.dan.eduinstitution.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dan.eduinstitution.model.UserCreateDto;
import ru.dan.eduinstitution.model.UserResponseDto;
import ru.dan.eduinstitution.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto dto) {
        log.info("Creating user with name: {}", dto.getName());
        UserResponseDto responseDto = userService.createUser(dto);
        log.info("User created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("Getting user by ID: {}", id);
        UserResponseDto responseDto = userService.getUserById(id);
        log.info("User retrieved with name: {}", responseDto.getName());
        return ResponseEntity.ok(responseDto);
    }
}
