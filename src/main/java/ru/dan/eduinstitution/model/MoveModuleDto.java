package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для перемещения модуля в другой курс.
 */
@Data
@Schema(description = "DTO for moving a module to a different course")
public class MoveModuleDto {

    @Schema(description = "ID of the course to move the module to", example = "2")
    private Long targetCourseId;
}