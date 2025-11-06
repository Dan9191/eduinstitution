package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления варианта ответа.
 */
@Data
@Schema(description = "DTO for updating an answer option")
public class AnswerOptionUpdateDto {

    @Schema(description = "Text of the answer option", example = "Paris")
    private String text;
    
    @Schema(description = "Indicates if this option is correct", example = "true")
    private Boolean isCorrect;
}