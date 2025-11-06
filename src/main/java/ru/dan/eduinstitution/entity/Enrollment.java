package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Сущность-связка между студентом и курсом.
 */
@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@IdClass(EnrollmentId.class)
@Schema(description = "Enrollment entity representing a link between student and course")
public class Enrollment {
    @Id
    @Column(name = "user_id")
    @Schema(description = "ID of the enrolled user", example = "1")
    private Long userId;

    @Id
    @Column(name = "course_id")
    @Schema(description = "ID of the enrolled course", example = "1")
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @Schema(description = "Student enrolled in the course")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    @Schema(description = "Course in which the student is enrolled")
    private Course course;

    @Schema(description = "Date when the enrollment was made", example = "2025-01-15")
    private LocalDate enrollDate;

    @Schema(description = "Status of the enrollment", example = "Active", allowableValues = {"Active", "Completed", "Dropped"})
    private String status; // Active, Completed, Dropped
}
