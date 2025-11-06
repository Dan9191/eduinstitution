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
import ru.dan.eduinstitution.model.LessonCreateDto;
import ru.dan.eduinstitution.model.LessonResponseDto;
import ru.dan.eduinstitution.model.LessonUpdateDto;
import ru.dan.eduinstitution.service.LessonService;

import java.util.List;

@Tag(name = "Lesson", description = "Operations related to lessons")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonController {

    private final LessonService lessonService;

    @Operation(
            summary = "Create a new lesson",
            description = "Creates a new lesson with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Lesson created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LessonResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Module not found")
            }
    )
    @PostMapping
    public ResponseEntity<LessonResponseDto> createLesson(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lesson details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LessonCreateDto.class))
            ) @Valid @RequestBody LessonCreateDto dto) {
        log.info("Creating lesson with title: {}", dto.getTitle());
        LessonResponseDto responseDto = lessonService.createLesson(dto);
        log.info("Lesson created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get lesson by ID",
            description = "Retrieves a lesson by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lesson retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LessonResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Lesson not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDto> getLessonById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the lesson")
            @PathVariable Long id) {
        log.info("Getting lesson by ID: {}", id);
        LessonResponseDto responseDto = lessonService.getLessonById(id);
        log.info("Lesson retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update lesson",
            description = "Updates a lesson with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LessonResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Lesson not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDto> updateLesson(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the lesson")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lesson details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LessonUpdateDto.class))
            ) @Valid @RequestBody LessonUpdateDto dto) {
        log.info("Updating lesson with ID: {}", id);
        LessonResponseDto responseDto = lessonService.updateLesson(id, dto);
        log.info("Lesson updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete lesson",
            description = "Deletes a lesson by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Lesson deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Lesson not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the lesson")
            @PathVariable Long id) {
        log.info("Deleting lesson with ID: {}", id);
        lessonService.deleteLesson(id);
        log.info("Lesson deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all lessons for a module",
            description = "Retrieves all lessons for a specific module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of module lessons retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LessonResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Module not found")
            }
    )
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<LessonResponseDto>> getLessonsByModuleId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the module")
            @PathVariable Long moduleId) {
        log.info("Getting lessons for module with ID: {}", moduleId);
        List<LessonResponseDto> responseDtos = lessonService.getLessonsByModuleId(moduleId);
        log.info("Retrieved {} lessons for module with ID: {}", responseDtos.size(), moduleId);
        return ResponseEntity.ok(responseDtos);
    }
}