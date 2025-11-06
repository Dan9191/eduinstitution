package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * DTO для обновления отзыва о курсе.
 */
@Data
@Schema(description = "DTO for updating a course review")
public class CourseReviewUpdateDto {

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Schema(description = "Rating for the course (1-5)", example = "5")
    private Integer rating;
    
    @Schema(description = "Comment in the review", example = "Great course with excellent content!")
    private String comment;
}