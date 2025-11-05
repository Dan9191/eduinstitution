package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Отзыв о курсе.
 */
@Entity
@Table(name = "course_reviews")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "CourseReview entity representing a review for a course")
public class CourseReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the course review", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @Schema(description = "Course that is being reviewed")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @Schema(description = "Student who wrote the review")
    private User student;

    @Schema(description = "Rating for the course (1-5)", example = "5", minimum = "1", maximum = "5")
    private Integer rating; // 1-5
    
    @Schema(description = "Comment in the review", example = "Great course with excellent content!")
    private String comment;
    
    @Schema(description = "Date and time when the review was created", example = "2025-01-15T16:45:00")
    private LocalDateTime createdAt;
}
