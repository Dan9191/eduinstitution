package ru.dan.eduinstitution.entity;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false)
    private Profile profile;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> coursesTaught;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Submission> submissions;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<QuizSubmission> quizSubmissions;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
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
        STUDENT, TEACHER, ADMIN;

        public static Role getRole(String roleName) {
            return Role.valueOf(roleName);
        }

    }

}