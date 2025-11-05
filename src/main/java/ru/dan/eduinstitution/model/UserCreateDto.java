package ru.dan.eduinstitution.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Модель для создания пользователя.
 */
@Data
public class UserCreateDto {

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Role cannot be null")
    @Pattern(regexp = "STUDENT|TEACHER|ADMIN", message = "Role must be STUDENT, TEACHER or ADMIN")
    private String role;

    private String bio;

    private String avatarUrl;

}
