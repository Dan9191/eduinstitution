package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания результата теста.
 */
@Data
@Schema(description = "DTO for creating a new quiz submission")
public class QuizSubmissionCreateDto {

    @NotNull(message = "Quiz ID cannot be null")
    @Schema(description = "ID of the quiz that was submitted", example = "1")
    private Long quizId;

    @NotNull(message = "Student ID cannot be null")
    @Schema(description = "ID of the student who submitted the quiz", example = "1")
    private Long studentId;
    
    @NotNull(message = "Score cannot be null")
    @Schema(description = "Score achieved in the quiz", example = "85")
    private Integer score;
}