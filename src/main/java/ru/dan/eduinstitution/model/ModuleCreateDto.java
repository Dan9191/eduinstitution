package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания модуля.
 */
@Data
@Schema(description = "DTO for creating a new module")
public class ModuleCreateDto {

    @NotNull(message = "Course ID cannot be null")
    @Schema(description = "ID of the course to which this module belongs", example = "1")
    private Long courseId;

    @NotNull(message = "Title cannot be null")
    @Schema(description = "Title of the module", example = "Java Fundamentals")
    private String title;
    
    @Schema(description = "Order index of the module", example = "1")
    private Integer orderIndex;
    
    @Schema(description = "Description of the module", example = "This module covers Java fundamentals...")
    private String description;
}