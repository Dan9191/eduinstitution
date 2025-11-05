package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Категория курса.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Category entity representing a course category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the category", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Name of the category", example = "Programming")
    private String name;
}
