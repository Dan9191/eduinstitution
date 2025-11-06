package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO для обновления задания.
 */
@Data
@Schema(description = "DTO for updating an assignment")
public class AssignmentUpdateDto {

    @Schema(description = "Title of the assignment", example = "Homework #1: Java Basics")
    private String title;
    
    @Schema(description = "Description of the assignment", example = "Complete the exercises on Java basics")
    private String description;
    
    @Schema(description = "Due date for the assignment", example = "2025-01-30")
    private LocalDate dueDate;
    
    @Schema(description = "Maximum score for the assignment", example = "100")
    private Integer maxScore;
}