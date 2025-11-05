package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Задание.
 */
@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Assignment entity representing a course assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the assignment", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    @Schema(description = "Lesson to which this assignment belongs")
    private Lesson lesson;

    @Schema(description = "Title of the assignment", example = "Homework #1: Java Basics")
    private String title;
    
    @Schema(description = "Description of the assignment", example = "Complete the exercises on Java basics")
    private String description;
    
    @Schema(description = "Due date for the assignment", example = "2025-01-30")
    private LocalDate dueDate;
    
    @Schema(description = "Maximum score for the assignment", example = "100")
    private Integer maxScore;

    @OneToMany(mappedBy = "assignment", fetch = FetchType.LAZY)
    @Schema(description = "List of submissions for this assignment")
    private List<Submission> submissions;
}
