package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией о варианте ответа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing an answer option")
public class AnswerOptionResponseDto {

    @Schema(description = "ID of the answer option", example = "1")
    private Long id;

    @Schema(description = "Text of the answer option", example = "Paris")
    private String text;
    
    @Schema(description = "Indicates if this option is correct", example = "true")
    private Boolean isCorrect;
    
    @Schema(description = "ID of the question to which this answer option belongs", example = "1")
    private Long questionId;
    
    @Schema(description = "Text of the question to which this answer option belongs", example = "What is the capital of France?")
    private String questionText;
}