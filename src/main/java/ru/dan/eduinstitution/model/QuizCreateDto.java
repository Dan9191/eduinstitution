package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания теста.
 */
@Data
@Schema(description = "DTO for creating a new quiz")
public class QuizCreateDto {

    @NotNull(message = "Module ID cannot be null")
    @Schema(description = "ID of the module to which this quiz belongs", example = "1")
    private Long moduleId;

    @NotNull(message = "Title cannot be null")
    @Schema(description = "Title of the quiz", example = "Midterm Exam")
    private String title;
    
    @Schema(description = "Time limit for the quiz in minutes", example = "60")
    private Integer timeLimit;
}