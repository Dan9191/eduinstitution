package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Тест.
 */
@Entity
@Table(name = "quizzes")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Quiz entity representing a course quiz/test")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the quiz", example = "1")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    @Schema(description = "Module to which this quiz belongs")
    private Module module;

    @Schema(description = "Title of the quiz", example = "Midterm Exam")
    private String title;
    
    @Schema(description = "Time limit for the quiz in minutes", example = "60")
    private Integer timeLimit; // в минутах

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "List of questions in this quiz")
    private List<Question> questions;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    @Schema(description = "List of submissions for this quiz")
    private List<QuizSubmission> submissions;
}
