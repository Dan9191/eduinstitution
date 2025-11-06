package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Модель для создания пользователя.
 */
@Data
@Schema(description = "DTO for creating a new user")
public class UserCreateDto {

    @NotNull(message = "Name cannot be null")
    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @NotNull(message = "Role cannot be null")
    @Pattern(regexp = "STUDENT|TEACHER|ADMIN", message = "Role must be STUDENT, TEACHER or ADMIN")
    @Schema(description = "Role of the user", example = "STUDENT")
    private String role;

    @Schema(description = "Biography of the user", example = "Software developer and teacher")
    private String bio;

    @Schema(description = "URL of the user's avatar", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

}
