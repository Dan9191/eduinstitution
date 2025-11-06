package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления урока.
 */
@Data
@Schema(description = "DTO for updating a lesson")
public class LessonUpdateDto {

    @Schema(description = "Title of the lesson", example = "Introduction to Java")
    private String title;
    
    @Schema(description = "Content of the lesson", example = "This lesson covers Java basics...")
    private String content;
    
    @Schema(description = "URL of the lesson video", example = "https://example.com/video1.mp4")
    private String videoUrl;
}