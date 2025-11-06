package ru.dan.eduinstitution.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import ru.dan.eduinstitution.config.BaseTestWithContext;
import ru.dan.eduinstitution.model.UserCreateDto;
import ru.dan.eduinstitution.model.UserResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebMvc
class UserIntegrationTest extends BaseTestWithContext {

    @Test
    @DisplayName("Тест создания пользователя с валидными данными")
    void createUser_ValidData_ReturnsCreatedUser() {
        // Given
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("John Doe");
        userCreateDto.setEmail("john.doe@example.com");
        userCreateDto.setRole("STUDENT");
        userCreateDto.setBio("Student bio");
        userCreateDto.setAvatarUrl("https://example.com/avatar.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreateDto> entity = new HttpEntity<>(userCreateDto, headers);

        // When
        UserResponseDto response = restTemplate.postForObject(
                restTemplate.getRootUri() + "/user/create", entity, UserResponseDto.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getRole()).isEqualTo("STUDENT");
        assertThat(response.getBio()).isEqualTo("Student bio");
        assertThat(response.getAvatarUrl()).isEqualTo("https://example.com/avatar.jpg");
    }

    @Test
    @DisplayName("Тест создания пользователя с невалидной ролью")
    void createUser_InvalidRole_Returns400() {
        // Given
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("Jane Doe");
        userCreateDto.setEmail("jane.doe@example.com");
        userCreateDto.setRole("INVALID_ROLE"); // Невалидная роль

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreateDto> entity = new HttpEntity<>(userCreateDto, headers);

        // When
        HttpStatusCode status = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", entity, UserResponseDto.class).getStatusCode();

        // Then
        assertThat(status.value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Тест создания пользователя без обязательного поля email")
    void createUser_MissingEmail_Returns400() {
        // Given
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("Jane Doe");
        userCreateDto.setRole("TEACHER");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreateDto> entity = new HttpEntity<>(userCreateDto, headers);

        // When
        HttpStatusCode status = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", entity, UserResponseDto.class).getStatusCode();

        // Then
        assertThat(status.value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Тест получения пользователя по ID")
    void getUserById_ValidId_ReturnsUser() {
        // Given
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("Test User");
        userCreateDto.setEmail("test.user@example.com");
        userCreateDto.setRole("STUDENT");
        userCreateDto.setBio("Test bio");

        HttpHeaders createHeaders = new HttpHeaders();
        createHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreateDto> createEntity = new HttpEntity<>(userCreateDto, createHeaders);

        // When
        UserResponseDto createdUser = restTemplate.postForObject(
                restTemplate.getRootUri() + "/user/create", createEntity, UserResponseDto.class);

        // Then
        // проверяем, что пользователь создан
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("Test User");

        // Получаем пользователя по ID
        String url = String.format("%s/user/%d", restTemplate.getRootUri(), createdUser.getId());
        UserResponseDto retrievedUser = restTemplate.getForObject(url, UserResponseDto.class);

        // Проверяем, что полученный пользователь совпадает с созданным
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getId()).isEqualTo(createdUser.getId());
        assertThat(retrievedUser.getName()).isEqualTo(createdUser.getName());
        assertThat(retrievedUser.getEmail()).isEqualTo(createdUser.getEmail());
        assertThat(retrievedUser.getRole()).isEqualTo(createdUser.getRole());
    }

    @Test
    @DisplayName("Тест получения несуществующего пользователя по ID")
    void getUserById_NonExistentId_Returns404() {
        // Given
        String url = String.format("%s/user/%d", restTemplate.getRootUri(), 9999L);

        // When
        HttpStatusCode status = restTemplate.getForEntity(url, UserResponseDto.class).getStatusCode();

        // Then
        assertThat(status.value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}