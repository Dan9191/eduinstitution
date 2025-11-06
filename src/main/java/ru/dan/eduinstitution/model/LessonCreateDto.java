package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для создания урока.
 */
@Data
@Schema(description = "DTO for creating a new lesson")
public class LessonCreateDto {

    @NotNull(message = "Module ID cannot be null")
    @Schema(description = "ID of the module to which this lesson belongs", example = "1")
    private Long moduleId;

    @NotNull(message = "Title cannot be null")
    @Schema(description = "Title of the lesson", example = "Introduction to Java")
    private String title;
    
    @Schema(description = "Content of the lesson", example = "This lesson covers Java basics...")
    private String content;
    
    @Schema(description = "URL of the lesson video", example = "https://example.com/video1.mp4")
    private String videoUrl;
}