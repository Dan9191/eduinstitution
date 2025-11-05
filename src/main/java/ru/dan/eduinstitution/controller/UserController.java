package ru.dan.eduinstitution.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided details",
        responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details for creation",
            required = true,
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = UserCreateDto.class))
        ) @Valid @RequestBody UserCreateDto dto) {
        log.info("Creating user with name: {}", dto.getName());
        UserResponseDto responseDto = userService.createUser(dto);
        log.info("User created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a user by their unique ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
        @Parameter(description = "Unique identifier of the user", required = true) 
        @PathVariable Long id) {
        log.info("Getting user by ID: {}", id);
        UserResponseDto responseDto = userService.getUserById(id);
        log.info("User retrieved with name: {}", responseDto.getName());
        return ResponseEntity.ok(responseDto);
    }
}
