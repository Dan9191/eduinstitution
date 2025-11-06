package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о результате теста.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a quiz submission")
public class QuizSubmissionResponseDto {

    @Schema(description = "ID of the quiz submission", example = "1")
    private Long id;

    @Schema(description = "Score achieved in the quiz", example = "85")
    private Integer score;
    
    @Schema(description = "Date and time when the quiz was taken", example = "2025-01-15T10:30:00")
    private LocalDateTime takenAt;
    
    @Schema(description = "ID of the quiz that was submitted", example = "1")
    private Long quizId;
    
    @Schema(description = "Title of the quiz that was submitted", example = "Midterm Exam")
    private String quizTitle;
    
    @Schema(description = "ID of the student who submitted the quiz", example = "1")
    private Long studentId;
    
    @Schema(description = "Name of the student who submitted the quiz", example = "John Doe")
    private String studentName;
}