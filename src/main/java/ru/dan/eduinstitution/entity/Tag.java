package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Тег.
 */
@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Tag entity representing a course tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the tag", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Name of the tag", example = "Java")
    private String name;

    @ManyToMany(mappedBy = "tags")
    @Schema(description = "List of courses associated with this tag")
    private Set<Course> courses = new HashSet<>();
}
