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
 * Результат теста.
 */
@Entity
@Table(name = "quiz_submissions")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "QuizSubmission entity representing a quiz submission by a student")
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the quiz submission", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @Schema(description = "Quiz that was submitted")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @Schema(description = "Student who submitted the quiz")
    private User student;

    @Schema(description = "Score achieved in the quiz", example = "85")
    private Integer score;
    
    @Schema(description = "Date and time when the quiz was taken", example = "2025-01-15T10:30:00")
    private LocalDateTime takenAt;
}
