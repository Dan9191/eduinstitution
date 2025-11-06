package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания тега.
 */
@Data
@Schema(description = "DTO for creating a new tag")
public class TagCreateDto {

    @NotNull(message = "Name cannot be null")
    @Schema(description = "Name of the tag", example = "Java")
    private String name;
}