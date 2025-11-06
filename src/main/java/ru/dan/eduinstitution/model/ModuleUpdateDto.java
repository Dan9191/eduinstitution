package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления модуля.
 */
@Data
@Schema(description = "DTO for updating a module")
public class ModuleUpdateDto {

    @Schema(description = "Title of the module", example = "Java Fundamentals")
    private String title;
    
    @Schema(description = "Order index of the module", example = "1")
    private Integer orderIndex;
    
    @Schema(description = "Description of the module", example = "This module covers Java fundamentals...")
    private String description;
}