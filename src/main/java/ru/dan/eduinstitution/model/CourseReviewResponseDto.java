package ru.dan.eduinstitution.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для отзыва о курсе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseReviewResponseDto {

    private Long id;
    private Long courseId;
    private String courseTitle;
    private Long studentId;
    private String studentName;
    private Integer rating; // 1-5
    private String comment;
    private LocalDateTime createdAt;
}