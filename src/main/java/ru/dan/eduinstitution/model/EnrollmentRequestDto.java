package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для запроса на запись студента на курс.
 */
@Data
@Schema(description = "DTO for enrolling a student to a course")
public class EnrollmentRequestDto {

    @NotNull(message = "Student ID cannot be null")
    @Schema(description = "ID of the student to enroll", example = "1")
    private Long studentId;

    @NotNull(message = "Course ID cannot be null")
    @Schema(description = "ID of the course to enroll to", example = "1")
    private Long courseId;
}