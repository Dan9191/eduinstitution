package ru.dan.eduinstitution.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dan.eduinstitution.model.CourseCreateDto;
import ru.dan.eduinstitution.model.CourseResponseDto;
import ru.dan.eduinstitution.model.CourseUpdateDto;
import ru.dan.eduinstitution.service.CourseService;

@Tag(name = "Course", description = "Operations related to course")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    @Operation(
            summary = "Create a new course",
            description = "Creates a new course with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Course created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<CourseResponseDto> createCourse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Course details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseCreateDto.class))
            ) @Valid @RequestBody CourseCreateDto dto) {
        log.info("Creating course with title: {}", dto.getTitle());
        CourseResponseDto responseDto = courseService.createCourse(dto);
        log.info("Course created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get course by ID",
            description = "Retrieves a course by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long id) {
        log.info("Getting course by ID: {}", id);
        CourseResponseDto responseDto = courseService.getCourseById(id);
        log.info("Course retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update a course",
            description = "Updates a course with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Course details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseUpdateDto.class))
            ) @Valid @RequestBody CourseUpdateDto dto) {
        log.info("Updating course with ID: {}", id);
        CourseResponseDto responseDto = courseService.updateCourse(id, dto);
        log.info("Course updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Get all courses with pagination",
            description = "Retrieves all courses with pagination support",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of courses retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping
    public ResponseEntity<Page<CourseResponseDto>> getAllCourses(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Retrieving all courses with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<CourseResponseDto> courses = courseService.getAllCourses(pageable);
        log.info("Retrieved {} courses", courses.getContent().size());
        return ResponseEntity.ok(courses);
    }
}
