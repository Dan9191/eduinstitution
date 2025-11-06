package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией о тесте.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a quiz")
public class QuizResponseDto {

    @Schema(description = "ID of the quiz", example = "1")
    private Long id;

    @Schema(description = "Title of the quiz", example = "Midterm Exam")
    private String title;
    
    @Schema(description = "Time limit for the quiz in minutes", example = "60")
    private Integer timeLimit;
    
    @Schema(description = "ID of the module to which this quiz belongs", example = "1")
    private Long moduleId;
    
    @Schema(description = "Title of the module to which this quiz belongs", example = "Java Fundamentals")
    private String moduleTitle;
}