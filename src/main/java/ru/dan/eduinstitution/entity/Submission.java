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
 * Ответ/решение студента на задание.
 */
@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Submission entity representing a student's assignment submission")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the submission", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    @Schema(description = "Assignment that was submitted")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @Schema(description = "Student who submitted the assignment")
    private User student;

    @Schema(description = "Date and time when the submission was made", example = "2025-01-15T14:30:00")
    private LocalDateTime submittedAt;
    
    @Schema(description = "Content of the submission", example = "The solution to the assignment...")
    private String content;
    
    @Schema(description = "Score given for the submission", example = "95")
    private Integer score;
    
    @Schema(description = "Feedback provided for the submission", example = "Good work, but consider optimization...")
    private String feedback;
}
