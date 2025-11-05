package ru.dan.eduinstitution.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для результата викторины.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionResponseDto {

    private Long id;
    private Long quizId;
    private String quizTitle;
    private Long studentId;
    private String studentName;
    private Integer score;
    private LocalDateTime takenAt;
}