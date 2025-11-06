package ru.dan.eduinstitution.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.dan.eduinstitution.config.BaseTestWithContext;
import ru.dan.eduinstitution.entity.Category;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.model.EnrollmentRequestDto;
import ru.dan.eduinstitution.model.EnrollmentResponseDto;
import ru.dan.eduinstitution.repository.CategoryRepository;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.UserRepository;
import ru.dan.eduinstitution.service.CategoryCacheService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для проверки основного функционала записи студента на курс.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicEnrollmentIntegrationTest extends BaseTestWithContext {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryCacheService categoryCacheService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        categoryCacheService.updateCache();
    }

    @Test
    @DisplayName("Проверка записи студента на курс")
    void testEnrollStudent() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> 
            new RuntimeException("Category with id " + categoryId + " not found"));

        // Создаем учителя
        User teacher = new User();
        teacher.setName("Test Teacher");
        teacher.setEmail("teacher.basic@example.com");
        teacher.setRole(User.Role.TEACHER);
        teacher = userRepository.save(teacher);

        // Создаем курс
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Course Description");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(30);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);

        // Создаем студента
        User student = new User();
        student.setName("Test Student");
        student.setEmail("student.basic@example.com");
        student.setRole(User.Role.STUDENT);
        student = userRepository.save(student);

        // Проверяем запись студента на курс
        EnrollmentRequestDto requestDto = new EnrollmentRequestDto();
        requestDto.setStudentId(student.getId());
        requestDto.setCourseId(course.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EnrollmentRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<EnrollmentResponseDto> response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/enrollment/enroll", entity, EnrollmentResponseDto.class);

        // Проверяем основные аспекты ответа
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(student.getId(), response.getBody().getStudentId());
        assertEquals(course.getId(), response.getBody().getCourseId());
        assertEquals("Active", response.getBody().getStatus());
    }
}