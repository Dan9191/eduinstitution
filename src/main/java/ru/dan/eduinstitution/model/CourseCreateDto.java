package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * Модель для создания пользователя.
 */
@Data
@Schema(description = "DTO for creating a new course")
public class CourseCreateDto {

    @NotNull(message = "Title cannot be null")
    @Schema(description = "Title of the course", example = "Java Programming")
    private String title;

    @Schema(description = "Description of the course")
    private String description;

    @NotNull(message = "Category cannot be null")
    @Schema(description = "Category id of the course")
    private Long categoryId;

    @NotNull(message = "Teacher cannot be null")
    @Schema(description = "Teachers user id")
    private Long teacherId;

    @Schema(description = "Course duration")
    private int duration;

    @Schema(description = "Start date of the course", example = "2025-01-15")
    private LocalDate startDate;

}
