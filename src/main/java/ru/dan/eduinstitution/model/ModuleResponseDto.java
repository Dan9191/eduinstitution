package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией о модуле.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a module")
public class ModuleResponseDto {

    @Schema(description = "ID of the module", example = "1")
    private Long id;

    @Schema(description = "Title of the module", example = "Java Fundamentals")
    private String title;
    
    @Schema(description = "Order index of the module", example = "1")
    private Integer orderIndex;
    
    @Schema(description = "Description of the module", example = "This module covers Java fundamentals...")
    private String description;
    
    @Schema(description = "ID of the course to which this module belongs", example = "1")
    private Long courseId;
    
    @Schema(description = "Title of the course to which this module belongs", example = "Java Programming")
    private String courseTitle;
}