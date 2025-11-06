package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления тега.
 */
@Data
@Schema(description = "DTO for updating a tag")
public class TagUpdateDto {

    @Schema(description = "Name of the tag", example = "Java")
    private String name;
}