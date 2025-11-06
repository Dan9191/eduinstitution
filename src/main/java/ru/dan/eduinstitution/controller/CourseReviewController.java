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
import ru.dan.eduinstitution.model.CourseReviewCreateDto;
import ru.dan.eduinstitution.model.CourseReviewResponseDto;
import ru.dan.eduinstitution.model.CourseReviewUpdateDto;
import ru.dan.eduinstitution.service.CourseReviewService;

import java.util.List;

@Tag(name = "CourseReview", description = "Operations related to course reviews")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/course-review")
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    @Operation(
            summary = "Add a new course review",
            description = "Adds a new course review with the provided details (addReview method)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Course review added successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseReviewResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Course or student not found"),
                    @ApiResponse(responseCode = "409", description = "Review already exists for this course and student")
            }
    )
    @PostMapping
    public ResponseEntity<CourseReviewResponseDto> addReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Course review details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReviewCreateDto.class))
            ) @Valid @RequestBody CourseReviewCreateDto dto) {
        log.info("Adding review for course ID: {} by student ID: {} with rating: {}", 
                dto.getCourseId(), dto.getStudentId(), dto.getRating());
        CourseReviewResponseDto responseDto = courseReviewService.addReview(dto);
        log.info("Review added with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get course review by ID",
            description = "Retrieves a course review by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course review retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseReviewResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Course review not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CourseReviewResponseDto> getCourseReviewById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course review")
            @PathVariable Long id) {
        log.info("Getting course review by ID: {}", id);
        CourseReviewResponseDto responseDto = courseReviewService.getCourseReviewById(id);
        log.info("Course review retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update course review",
            description = "Updates a course review with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course review updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseReviewResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Course review not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CourseReviewResponseDto> updateCourseReview(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course review")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Course review details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReviewUpdateDto.class))
            ) @Valid @RequestBody CourseReviewUpdateDto dto) {
        log.info("Updating course review with ID: {}", id);
        CourseReviewResponseDto responseDto = courseReviewService.updateCourseReview(id, dto);
        log.info("Course review updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete course review",
            description = "Deletes a course review by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Course review deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Course review not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseReview(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course review")
            @PathVariable Long id) {
        log.info("Deleting course review with ID: {}", id);
        courseReviewService.deleteCourseReview(id);
        log.info("Course review deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all reviews for a course (getReviewsByCourse method)",
            description = "Retrieves all reviews for a specific course",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of course reviews retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseReviewResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseReviewResponseDto>> getReviewsByCourse(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long courseId) {
        log.info("Getting reviews for course with ID: {}", courseId);
        List<CourseReviewResponseDto> responseDtos = courseReviewService.getReviewsByCourse(courseId);
        log.info("Retrieved {} reviews for course with ID: {}", responseDtos.size(), courseId);
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "Get all reviews by a student",
            description = "Retrieves all reviews written by a specific student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of course reviews retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CourseReviewResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CourseReviewResponseDto>> getReviewsByStudent(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the student")
            @PathVariable Long studentId) {
        log.info("Getting reviews by student with ID: {}", studentId);
        List<CourseReviewResponseDto> responseDtos = courseReviewService.getReviewsByStudent(studentId);
        log.info("Retrieved {} reviews by student with ID: {}", responseDtos.size(), studentId);
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "Get average rating for a course",
            description = "Retrieves the average rating for a specific course",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Average rating retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Double.class))),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    @GetMapping("/course/{courseId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByCourse(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long courseId) {
        log.info("Getting average rating for course with ID: {}", courseId);
        Double averageRating = courseReviewService.getAverageRatingByCourse(courseId);
        log.info("Average rating for course with ID {} is: {}", courseId, averageRating);
        return ResponseEntity.ok(averageRating);
    }
}