package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией об ответе/решении студента.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a submission")
public class SubmissionResponseDto {

    @Schema(description = "ID of the submission", example = "1")
    private Long id;

    @Schema(description = "Date and time when the submission was made", example = "2025-01-15T14:30:00")
    private LocalDateTime submittedAt;
    
    @Schema(description = "Content of the submission", example = "The solution to the assignment...")
    private String content;
    
    @Schema(description = "Score given for the submission", example = "95")
    private Integer score;
    
    @Schema(description = "Feedback provided for the submission", example = "Good work, but consider optimization...")
    private String feedback;
    
    @Schema(description = "ID of the assignment that was submitted", example = "1")
    private Long assignmentId;
    
    @Schema(description = "Title of the assignment that was submitted", example = "Homework #1: Java Basics")
    private String assignmentTitle;
    
    @Schema(description = "ID of the student who submitted", example = "1")
    private Long studentId;
    
    @Schema(description = "Name of the student who submitted", example = "John Doe")
    private String studentName;
}