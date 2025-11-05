package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Тематический раздел внутри курса.
 */
@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Module entity representing a thematic section within a course")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the module", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @Schema(description = "Course to which this module belongs")
    private Course course;

    @Schema(description = "Title of the module", example = "Java Fundamentals")
    private String title;
    
    @Schema(description = "Order index of the module", example = "1")
    private Integer orderIndex;
    
    @Schema(description = "Description of the module", example = "This module covers Java fundamentals...")
    private String description;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "List of lessons in this module")
    private List<Lesson> lessons;

    @OneToOne(mappedBy = "module", fetch = FetchType.LAZY)
    @Schema(description = "Quiz associated with this module")
    private Quiz quiz;
}
