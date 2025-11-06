package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO для обновления курса.
 */
@Data
@Schema(description = "DTO for updating a course")
public class CourseUpdateDto {

    @Schema(description = "Title of the course", example = "Java Programming")
    private String title;

    @Schema(description = "Description of the course", example = "Complete course on Java programming")
    private String description;

    @Schema(description = "ID of the category", example = "1")
    private Long categoryId;

    @Schema(description = "ID of the teacher", example = "1")
    private Long teacherId;

    @Schema(description = "Duration of the course in days", example = "30")
    private Integer duration;

    @Schema(description = "Start date of the course", example = "2025-01-15")
    private LocalDate startDate;

    @Schema(description = "Set of tag IDs associated with this course", example = "[1, 2, 3]")
    private Set<Long> tagIds;
}