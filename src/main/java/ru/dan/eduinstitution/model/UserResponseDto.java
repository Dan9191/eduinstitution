package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Модель для получения данных о пользователе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing user information")
public class UserResponseDto {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;
    
    @Schema(description = "Name of the user", example = "John Doe")
    private String name;
    
    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "Role of the user", example = "STUDENT")
    private String role;
    
    @Schema(description = "Biography of the user", example = "Software developer and teacher")
    private String bio;
    
    @Schema(description = "URL of the user's avatar", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
    
    @Schema(description = "List of courses taught by this user")
    private List<CourseResponseDto> coursesTaught;
    
    @Schema(description = "List of enrollments of this user")
    private List<EnrollmentResponseDto> enrollments;
    
    @Schema(description = "List of submissions made by this user")
    private List<SubmissionResponseDto> submissions;
    
    @Schema(description = "List of quiz submissions made by this user")
    private List<QuizSubmissionResponseDto> quizSubmissions;
    
    @Schema(description = "List of course reviews made by this user")
    private List<CourseReviewResponseDto> courseReviews;

}
