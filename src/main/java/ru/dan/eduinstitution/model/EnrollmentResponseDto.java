package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO для ответа о записи студента на курс.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing a student enrollment in a course")
public class EnrollmentResponseDto {

    @Schema(description = "ID of the student enrolled", example = "1")
    private Long studentId;

    @Schema(description = "Name of the enrolled student", example = "John Doe")
    private String studentName;

    @Schema(description = "ID of the enrolled course", example = "1")
    private Long courseId;

    @Schema(description = "Title of the enrolled course", example = "Java Programming")
    private String courseTitle;

    @Schema(description = "Enrollment date", example = "2025-01-15")
    private LocalDate enrollDate;

    @Schema(description = "Status of the enrollment", example = "Active")
    private String status;
}