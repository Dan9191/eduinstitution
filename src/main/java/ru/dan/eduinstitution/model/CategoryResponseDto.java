package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для получения данных о категории.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing category information")
public class CategoryResponseDto {

    @Schema(description = "Unique identifier of the category", example = "1")
    private Long id;

    @Schema(description = "Name of the category", example = "Programming", required = true)
    private String name;
}