package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * DTO для обновления результата теста.
 */
@Data
@Schema(description = "DTO for updating a quiz submission")
public class QuizSubmissionUpdateDto {

    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must be at most 100")
    @Schema(description = "Score achieved in the quiz", example = "85")
    private Integer score;
}