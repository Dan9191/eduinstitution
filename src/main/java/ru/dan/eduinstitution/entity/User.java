package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dan.eduinstitution.model.UserCreateDto;

import java.util.List;

/**
 * Пользователь.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User entity representing a user in the educational institution")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Role of the user", example = "STUDENT")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false)
    @Schema(description = "Profile of the user")
    private Profile profile;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @Schema(description = "List of courses taught by this user")
    private List<Course> coursesTaught;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Schema(description = "List of enrollments of this user")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Schema(description = "List of submissions made by this user")
    private List<Submission> submissions;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Schema(description = "List of quiz submissions made by this user")
    private List<QuizSubmission> quizSubmissions;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Schema(description = "List of course reviews made by this user")
    private List<CourseReview> courseReviews;

    public User(UserCreateDto userCreateDto) {
        this.name = userCreateDto.getName();
        this.email = userCreateDto.getEmail();
        this.role = Role.getRole(userCreateDto.getRole());
        Profile profile = new Profile();
        this.profile = profile;
        profile.setBio(userCreateDto.getBio());
        profile.setUser(this);
        profile.setAvatarUrl(userCreateDto.getAvatarUrl());
    }


    public enum Role {
        @Schema(description = "Student role")
        STUDENT, 
        @Schema(description = "Teacher role")
        TEACHER, 
        @Schema(description = "Admin role")
        ADMIN;

        public static Role getRole(String roleName) {
            return Role.valueOf(roleName);
        }

    }

}