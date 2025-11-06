package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания варианта ответа.
 */
@Data
@Schema(description = "DTO for creating a new answer option")
public class AnswerOptionCreateDto {

    @NotNull(message = "Question ID cannot be null")
    @Schema(description = "ID of the question to which this answer option belongs", example = "1")
    private Long questionId;

    @NotNull(message = "Text cannot be null")
    @Schema(description = "Text of the answer option", example = "Paris")
    private String text;
    
    @Schema(description = "Indicates if this option is correct", example = "true")
    private Boolean isCorrect;
}