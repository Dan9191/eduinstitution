package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Профиль пользователя.
 */
@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Profile entity representing user profile information")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the profile", example = "1")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(description = "User associated with this profile")
    private User user;

    @Schema(description = "Biography of the user", example = "Software developer and teacher")
    private String bio;

    @Schema(description = "URL of the user's avatar", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
}
