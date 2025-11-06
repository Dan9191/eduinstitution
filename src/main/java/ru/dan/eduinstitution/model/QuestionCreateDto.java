package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания вопроса.
 */
@Data
@Schema(description = "DTO for creating a new question")
public class QuestionCreateDto {

    @NotNull(message = "Quiz ID cannot be null")
    @Schema(description = "ID of the quiz to which this question belongs", example = "1")
    private Long quizId;

    @NotNull(message = "Text cannot be null")
    @Schema(description = "Text of the question", example = "What is the capital of France?")
    private String text;
    
    @NotNull(message = "Type cannot be null")
    @Schema(description = "Type of the question", allowableValues = {"SINGLE_CHOICE", "MULTIPLE_CHOICE", "TEXT"})
    private String type;
}