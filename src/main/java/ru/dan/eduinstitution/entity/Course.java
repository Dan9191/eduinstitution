package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Курс.
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Course entity representing an educational course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the course", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Title of the course", example = "Java Programming")
    private String title;

    @Schema(description = "Description of the course", example = "Complete course on Java programming")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @Schema(description = "Category of the course")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @Schema(description = "Teacher teaching this course")
    private User teacher;

    @Schema(description = "Duration of the course in days", example = "30")
    private Integer duration; // в днях
    
    @Schema(description = "Start date of the course", example = "2025-01-15")
    private LocalDate startDate;

    // Связи
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "List of modules in this course")
    private List<Module> modules;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "List of enrollments in this course")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Schema(description = "List of reviews for this course")
    private List<CourseReview> reviews;

    @ManyToMany
    @JoinTable(
            name = "course_tag",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Schema(description = "List of tags associated with this course")
    private Set<Tag> tags = new HashSet<>();
}