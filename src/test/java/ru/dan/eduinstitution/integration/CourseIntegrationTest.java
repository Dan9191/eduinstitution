package ru.dan.eduinstitution.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.dan.eduinstitution.config.BaseTestWithContext;
import ru.dan.eduinstitution.entity.Category;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.repository.CategoryRepository;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.UserRepository;
import ru.dan.eduinstitution.service.CategoryCacheService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для CourseController.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseIntegrationTest extends BaseTestWithContext {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryCacheService categoryCacheService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Очистка тестовых данных
        courseRepository.deleteAll();
        userRepository.deleteAll();

        // Добавление тестовых пользователей
        User teacher1 = new User();
        teacher1.setName("John Doe");
        teacher1.setEmail("john.doe.course@example.com");
        teacher1.setRole(User.Role.TEACHER);
        userRepository.save(teacher1);

        User teacher2 = new User();
        teacher2.setName("Jane Smith");
        teacher2.setEmail("jane.smith.course@example.com");
        teacher2.setRole(User.Role.TEACHER);
        userRepository.save(teacher2);

        // Обновление кеша категорий
        categoryCacheService.updateCache();

        // Добавление тестовых курсов
        Category category = categoryRepository.findById(1L).orElseThrow(() -> new RuntimeException("Category not found"));

        Course course1 = new Course();
        course1.setTitle("Java Programming");
        course1.setDescription("Complete Java programming course");
        course1.setCategory(category);
        course1.setTeacher(teacher1);
        course1.setDuration(30);
        course1.setStartDate(LocalDate.now());
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setTitle("Web Development");
        course2.setDescription("Complete web development course");
        course2.setCategory(category);
        course2.setTeacher(teacher2);
        course2.setDuration(45);
        course2.setStartDate(LocalDate.now().plusDays(7));
        courseRepository.save(course2);

        Course course3 = new Course();
        course3.setTitle("Mobile Development");
        course3.setDescription("Complete mobile development course");
        course3.setCategory(category);
        course3.setTeacher(teacher1);
        course3.setDuration(60);
        course3.setStartDate(LocalDate.now().plusDays(14));
        courseRepository.save(course3);
    }

    @Test
    @DisplayName("Тест получения всех курсов с пагинацией")
    void getAllCoursesWithPagination_ReturnsPagedCourses() {
        // When - запрос с пагинацией (page=0, size=2)
        ResponseEntity<String> response = restTemplate.getForEntity("/course?page=0&size=2", String.class);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Десериализуем JSON в Map и проверяем пагинацию
        try {
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            
            // Проверяем поля пагинации
            assertEquals(2, (Integer)responseBody.get("size")); // размер страницы
            assertEquals(0, (Integer)responseBody.get("number")); // номер страницы
            assertEquals(3L, ((Integer)responseBody.get("totalElements")).longValue()); // всего элементов
            assertEquals(2, (Integer)responseBody.get("numberOfElements")); // элементов на странице
            assertEquals(2, (Integer)responseBody.get("totalPages")); // всего страниц
            assertEquals(true, responseBody.get("first")); // первая страница
            assertEquals(false, responseBody.get("last")); // не последняя страница
            
            // Проверяем содержимое
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> content = (java.util.List<Map<String, Object>>)responseBody.get("content");
            assertEquals(2, content.size()); // 2 курса на странице
            
        } catch (IOException e) {
            fail("Failed to parse JSON response: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Тест получения второй страницы курсов с пагинацией")
    void getAllCoursesWithPagination_SecondPage_ReturnsPagedCourses() {
        // When - запрос второй страницы (page=1, size=2)
        ResponseEntity<String> response = restTemplate.getForEntity("/course?page=1&size=2", String.class);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Десериализуем JSON в Map и проверяем пагинацию
        try {
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            
            // Проверяем поля пагинации
            assertEquals(2, (Integer)responseBody.get("size")); // размер страницы
            assertEquals(1, (Integer)responseBody.get("number")); // номер страницы
            assertEquals(3L, ((Integer)responseBody.get("totalElements")).longValue()); // всего элементов
            assertEquals(1, (Integer)responseBody.get("numberOfElements")); // элементов на странице
            assertEquals(2, (Integer)responseBody.get("totalPages")); // всего страниц
            assertEquals(false, responseBody.get("first")); // не первая страница
            assertEquals(true, responseBody.get("last")); // последняя страница
            
            // Проверяем содержимое
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> content = (java.util.List<Map<String, Object>>)responseBody.get("content");
            assertEquals(1, content.size()); // 1 курс на второй странице
            
        } catch (IOException e) {
            fail("Failed to parse JSON response: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Тест получения всех курсов с пагинацией по умолчанию")
    void getAllCoursesWithoutPagination_ReturnsPagedCoursesWithDefaultSize() {
        // When - запрос без параметров пагинации (должно использовать значения по умолчанию)
        ResponseEntity<String> response = restTemplate.getForEntity("/course", String.class);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Десериализуем JSON в Map и проверяем пагинацию
        try {
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            
            // Проверяем поля пагинации (по умолчанию size=10)
            assertEquals(10, (Integer)responseBody.get("size")); // размер страницы по умолчанию
            assertEquals(0, (Integer)responseBody.get("number")); // номер страницы
            assertEquals(3L, ((Integer)responseBody.get("totalElements")).longValue()); // всего элементов
            assertEquals(3, (Integer)responseBody.get("numberOfElements")); // элементов на странице (меньше размера страницы)
            assertEquals(1, (Integer)responseBody.get("totalPages")); // всего страниц
            
            // Проверяем содержимое
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> content = (java.util.List<Map<String, Object>>)responseBody.get("content");
            assertEquals(3, content.size()); // 3 курса всего
            
        } catch (IOException e) {
            fail("Failed to parse JSON response: " + e.getMessage());
        }
    }
}