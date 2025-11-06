package ru.dan.eduinstitution.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dan.eduinstitution.model.AssignmentCreateDto;
import ru.dan.eduinstitution.model.AssignmentResponseDto;
import ru.dan.eduinstitution.model.AssignmentUpdateDto;
import ru.dan.eduinstitution.service.AssignmentService;

import java.util.List;

@Tag(name = "Assignment", description = "Operations related to assignments")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/assignment")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Operation(
            summary = "Create a new assignment",
            description = "Creates a new assignment with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Assignment created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Lesson not found")
            }
    )
    @PostMapping
    public ResponseEntity<AssignmentResponseDto> createAssignment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Assignment details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssignmentCreateDto.class))
            ) @Valid @RequestBody AssignmentCreateDto dto) {
        log.info("Creating assignment with title: {}", dto.getTitle());
        AssignmentResponseDto responseDto = assignmentService.createAssignment(dto);
        log.info("Assignment created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get assignment by ID",
            description = "Retrieves an assignment by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Assignment retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Assignment not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> getAssignmentById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the assignment")
            @PathVariable Long id) {
        log.info("Getting assignment by ID: {}", id);
        AssignmentResponseDto responseDto = assignmentService.getAssignmentById(id);
        log.info("Assignment retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update assignment",
            description = "Updates an assignment with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Assignment updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Assignment not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> updateAssignment(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the assignment")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Assignment details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssignmentUpdateDto.class))
            ) @Valid @RequestBody AssignmentUpdateDto dto) {
        log.info("Updating assignment with ID: {}", id);
        AssignmentResponseDto responseDto = assignmentService.updateAssignment(id, dto);
        log.info("Assignment updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete assignment",
            description = "Deletes an assignment by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Assignment deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Assignment not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the assignment")
            @PathVariable Long id) {
        log.info("Deleting assignment with ID: {}", id);
        assignmentService.deleteAssignment(id);
        log.info("Assignment deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all assignments for a lesson",
            description = "Retrieves all assignments for a specific lesson",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of lesson assignments retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Lesson not found")
            }
    )
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<AssignmentResponseDto>> getAssignmentsByLessonId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the lesson")
            @PathVariable Long lessonId) {
        log.info("Getting assignments for lesson with ID: {}", lessonId);
        List<AssignmentResponseDto> responseDtos = assignmentService.getAssignmentsByLessonId(lessonId);
        log.info("Retrieved {} assignments for lesson with ID: {}", responseDtos.size(), lessonId);
        return ResponseEntity.ok(responseDtos);
    }
}