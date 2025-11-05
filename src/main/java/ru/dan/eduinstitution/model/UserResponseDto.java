package ru.dan.eduinstitution.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dan.eduinstitution.entity.User;

import java.util.List;

/**
 * Модель для получения данных о пользователе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String bio;
    private String avatarUrl;
    private List<CourseResponseDto> coursesTaught;
    private List<EnrollmentResponseDto> enrollments;
    private List<SubmissionResponseDto> submissions;
    private List<QuizSubmissionResponseDto> quizSubmissions;
    private List<CourseReviewResponseDto> courseReviews;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();

        if (user.getProfile() != null) {
            this.bio = user.getProfile().getBio();
            this.avatarUrl = user.getProfile().getAvatarUrl();
        }

        // Обратите внимание: в этом конструкторе мы не можем заполнить вложенные DTO,
        // так как это требует дополнительной логики для преобразования сущностей в DTO
        // Эта логика будет реализована в сервисе
    }
}
