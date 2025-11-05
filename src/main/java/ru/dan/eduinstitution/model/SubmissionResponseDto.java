package ru.dan.eduinstitution.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для представленной работы.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponseDto {

    private Long id;
    private Long assignmentId;
    private String assignmentTitle;
    private Long studentId;
    private String studentName;
    private LocalDateTime submittedAt;
    private String content;
    private Integer score;
    private String feedback;
}