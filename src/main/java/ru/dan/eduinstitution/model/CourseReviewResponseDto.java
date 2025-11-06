package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией об отзыве о курсе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a course review")
public class CourseReviewResponseDto {

    @Schema(description = "ID of the course review", example = "1")
    private Long id;

    @Schema(description = "Rating for the course (1-5)", example = "5")
    private Integer rating;
    
    @Schema(description = "Comment in the review", example = "Great course with excellent content!")
    private String comment;
    
    @Schema(description = "Date and time when the review was created", example = "2025-01-15T16:45:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "ID of the course being reviewed", example = "1")
    private Long courseId;
    
    @Schema(description = "Title of the course being reviewed", example = "Java Programming")
    private String courseTitle;
    
    @Schema(description = "ID of the student who wrote the review", example = "1")
    private Long studentId;
    
    @Schema(description = "Name of the student who wrote the review", example = "John Doe")
    private String studentName;
}