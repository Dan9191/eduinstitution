package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO для ответа с информацией о задании.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing an assignment")
public class AssignmentResponseDto {

    @Schema(description = "ID of the assignment", example = "1")
    private Long id;

    @Schema(description = "Title of the assignment", example = "Homework #1: Java Basics")
    private String title;
    
    @Schema(description = "Description of the assignment", example = "Complete the exercises on Java basics")
    private String description;
    
    @Schema(description = "Due date for the assignment", example = "2025-01-30")
    private LocalDate dueDate;
    
    @Schema(description = "Maximum score for the assignment", example = "100")
    private Integer maxScore;
    
    @Schema(description = "ID of the lesson to which this assignment belongs", example = "1")
    private Long lessonId;
    
    @Schema(description = "Title of the lesson to which this assignment belongs", example = "Introduction to Java")
    private String lessonTitle;
}