package ru.dan.eduinstitution.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO для курса.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

    private Long id;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long teacherId;
    private String teacherName;
    private Integer duration; // в днях
    private LocalDate startDate;
}