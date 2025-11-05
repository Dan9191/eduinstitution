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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Урок.
 */
@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Lesson entity representing a course lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the lesson", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    @Schema(description = "Module to which this lesson belongs")
    private Module module;

    @Schema(description = "Title of the lesson", example = "Introduction to Java")
    private String title;
    
    @Schema(description = "Content of the lesson", example = "This lesson covers Java basics...")
    private String content;
    
    @Schema(description = "URL of the lesson video", example = "https://example.com/video1.mp4")
    private String videoUrl;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "List of assignments for this lesson")
    private List<Assignment> assignments;
}
