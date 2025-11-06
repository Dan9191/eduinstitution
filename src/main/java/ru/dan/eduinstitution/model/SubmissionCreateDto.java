package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания ответа/решения студента.
 */
@Data
@Schema(description = "DTO for creating a new submission")
public class SubmissionCreateDto {

    @NotNull(message = "Student ID cannot be null")
    @Schema(description = "ID of the student who is submitting", example = "1")
    private Long studentId;

    @NotNull(message = "Assignment ID cannot be null")
    @Schema(description = "ID of the assignment being submitted", example = "1")
    private Long assignmentId;

    @NotNull(message = "Content cannot be null")
    @Schema(description = "Content of the submission", example = "The solution to the assignment...")
    private String content;
}