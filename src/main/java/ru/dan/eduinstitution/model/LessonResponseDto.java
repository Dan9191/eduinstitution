package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией об уроке.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a lesson")
public class LessonResponseDto {

    @Schema(description = "ID of the lesson", example = "1")
    private Long id;

    @Schema(description = "Title of the lesson", example = "Introduction to Java")
    private String title;
    
    @Schema(description = "Content of the lesson", example = "This lesson covers Java basics...")
    private String content;
    
    @Schema(description = "URL of the lesson video", example = "https://example.com/video1.mp4")
    private String videoUrl;
    
    @Schema(description = "ID of the module to which this lesson belongs", example = "1")
    private Long moduleId;
    
    @Schema(description = "Title of the module to which this lesson belongs", example = "Java Fundamentals")
    private String moduleTitle;
}