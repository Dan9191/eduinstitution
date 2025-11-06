package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления вопроса.
 */
@Data
@Schema(description = "DTO for updating a question")
public class QuestionUpdateDto {

    @Schema(description = "Text of the question", example = "What is the capital of France?")
    private String text;
    
    @Schema(description = "Type of the question", allowableValues = {"SINGLE_CHOICE", "MULTIPLE_CHOICE", "TEXT"})
    private String type;
}