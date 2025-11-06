package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * DTO для оценки ответа/решения студента.
 */
@Data
@Schema(description = "DTO for grading a submission")
public class SubmissionGradeDto {

    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must be at most 100")
    @Schema(description = "Score given for the submission", example = "95")
    private Integer score;
    
    @Schema(description = "Feedback provided for the submission", example = "Good work, but consider optimization...")
    private String feedback;
}