package ru.dan.eduinstitution.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO для записи на курс.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDto {

    private Long userId;
    private Long courseId;
    private Long studentId;
    private String studentName;
    private String courseTitle;
    private LocalDate enrollDate;
    private String status; // Active, Completed, Dropped
}