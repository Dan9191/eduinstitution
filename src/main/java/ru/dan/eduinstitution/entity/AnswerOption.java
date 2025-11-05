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

/**
 * Вариант ответа.
 */
@Entity
@Table(name = "answer_options")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "AnswerOption entity representing an answer option for a quiz question")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the answer option", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @Schema(description = "Question to which this answer option belongs")
    private Question question;

    @Schema(description = "Text of the answer option", example = "Paris")
    private String text;
    
    @Schema(description = "Indicates if this option is correct", example = "true")
    private boolean isCorrect;
}
