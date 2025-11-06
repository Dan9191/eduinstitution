package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания отзыва о курсе.
 */
@Data
@Schema(description = "DTO for creating a new course review")
public class CourseReviewCreateDto {

    @NotNull(message = "Course ID cannot be null")
    @Schema(description = "ID of the course being reviewed", example = "1")
    private Long courseId;

    @NotNull(message = "Student ID cannot be null")
    @Schema(description = "ID of the student writing the review", example = "1")
    private Long studentId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @NotNull(message = "Rating cannot be null")
    @Schema(description = "Rating for the course (1-5)", example = "5")
    private Integer rating;
    
    @Schema(description = "Comment in the review", example = "Great course with excellent content!")
    private String comment;
}