package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для изменения порядка модуля.
 */
@Data
@Schema(description = "DTO for reordering a module within a course")
public class ReorderModuleDto {

    @Schema(description = "New order index for the module", example = "2")
    private Integer newOrderIndex;
}