package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией о теге.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a tag")
public class TagResponseDto {

    @Schema(description = "ID of the tag", example = "1")
    private Long id;

    @Schema(description = "Name of the tag", example = "Java")
    private String name;
}