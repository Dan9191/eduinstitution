package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления теста.
 */
@Data
@Schema(description = "DTO for updating a quiz")
public class QuizUpdateDto {

    @Schema(description = "Title of the quiz", example = "Midterm Exam")
    private String title;
    
    @Schema(description = "Time limit for the quiz in minutes", example = "60")
    private Integer timeLimit;
}