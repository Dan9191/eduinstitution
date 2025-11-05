package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.util.List;

/**
 * Вопрос.
 */
@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Question entity representing a quiz question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the question", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @Schema(description = "Quiz to which this question belongs")
    private Quiz quiz;

    @Schema(description = "Text of the question", example = "What is the capital of France?")
    private String text;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of the question", allowableValues = {"SINGLE_CHOICE", "MULTIPLE_CHOICE", "TEXT"})
    private QuestionType type;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "List of answer options for this question")
    private List<AnswerOption> options;

    public enum QuestionType {
        @Schema(description = "Single choice question type")
        SINGLE_CHOICE, 
        @Schema(description = "Multiple choice question type")
        MULTIPLE_CHOICE, 
        @Schema(description = "Text answer question type")
        TEXT
    }
}
