package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией о вопросе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a question")
public class QuestionResponseDto {

    @Schema(description = "ID of the question", example = "1")
    private Long id;

    @Schema(description = "Text of the question", example = "What is the capital of France?")
    private String text;
    
    @Schema(description = "Type of the question", allowableValues = {"SINGLE_CHOICE", "MULTIPLE_CHOICE", "TEXT"})
    private String type;
    
    @Schema(description = "ID of the quiz to which this question belongs", example = "1")
    private Long quizId;
    
    @Schema(description = "Title of the quiz to which this question belongs", example = "Midterm Exam")
    private String quizTitle;
}