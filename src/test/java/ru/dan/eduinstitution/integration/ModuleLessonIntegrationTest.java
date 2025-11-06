package ru.dan.eduinstitution.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import ru.dan.eduinstitution.model.LessonCreateDto;
import ru.dan.eduinstitution.model.LessonResponseDto;
import ru.dan.eduinstitution.model.ModuleCreateDto;
import ru.dan.eduinstitution.model.ModuleResponseDto;
import ru.dan.eduinstitution.repository.CategoryRepository;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.UserRepository;
import ru.dan.eduinstitution.service.CategoryCacheService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для модулей и уроков.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModuleLessonIntegrationTest extends BaseTestWithContext {

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
    @DisplayName("Проверка CRUD операций для модулей и уроков")
    void testModuleAndLessonCrudOperations() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> 
            new RuntimeException("Category with id " + categoryId + " not found"));

        User teacher = new User();
        teacher.setName("Test Teacher");
        teacher.setEmail("teacher.module.lesson@example.com");
        teacher.setRole(User.Role.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = new Course();
        course.setTitle("Test Course for Modules");
        course.setDescription("Test Course Description");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(30);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);

        // Проверяем создание модуля
        ModuleCreateDto moduleCreateDto = new ModuleCreateDto();
        moduleCreateDto.setCourseId(course.getId());
        moduleCreateDto.setTitle("Java Fundamentals");
        moduleCreateDto.setOrderIndex(1);
        moduleCreateDto.setDescription("This module covers Java fundamentals");

        HttpHeaders moduleHeaders = new HttpHeaders();
        moduleHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ModuleCreateDto> moduleEntity = new HttpEntity<>(moduleCreateDto, moduleHeaders);

        ResponseEntity<ModuleResponseDto> createModuleResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/module", moduleEntity, ModuleResponseDto.class);

        assertEquals(HttpStatus.CREATED, createModuleResponse.getStatusCode());
        assertNotNull(createModuleResponse.getBody());
        assertEquals("Java Fundamentals", createModuleResponse.getBody().getTitle());
        assertEquals(course.getId(), createModuleResponse.getBody().getCourseId());
        assertEquals(course.getTitle(), createModuleResponse.getBody().getCourseTitle());

        Long moduleId = createModuleResponse.getBody().getId();
        assertNotNull(moduleId);

        // Проверяем получение модуля по ID
        ResponseEntity<ModuleResponseDto> getModuleResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/module/" + moduleId, ModuleResponseDto.class);

        assertEquals(HttpStatus.OK, getModuleResponse.getStatusCode());
        assertNotNull(getModuleResponse.getBody());
        assertEquals(moduleId, getModuleResponse.getBody().getId());
        assertEquals("Java Fundamentals", getModuleResponse.getBody().getTitle());

        // Проверяем создание урока
        LessonCreateDto lessonCreateDto = new LessonCreateDto();
        lessonCreateDto.setModuleId(moduleId);
        lessonCreateDto.setTitle("Introduction to Java");
        lessonCreateDto.setContent("This lesson covers Java basics...");
        lessonCreateDto.setVideoUrl("https://example.com/video1.mp4");

        HttpHeaders lessonHeaders = new HttpHeaders();
        lessonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LessonCreateDto> lessonEntity = new HttpEntity<>(lessonCreateDto, lessonHeaders);

        ResponseEntity<LessonResponseDto> createLessonResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/lesson", lessonEntity, LessonResponseDto.class);

        assertEquals(HttpStatus.CREATED, createLessonResponse.getStatusCode());
        assertNotNull(createLessonResponse.getBody());
        assertEquals("Introduction to Java", createLessonResponse.getBody().getTitle());
        assertEquals(moduleId, createLessonResponse.getBody().getModuleId());
        assertEquals("Java Fundamentals", createLessonResponse.getBody().getModuleTitle());

        Long lessonId = createLessonResponse.getBody().getId();
        assertNotNull(lessonId);

        // Проверяем получение урока по ID
        ResponseEntity<LessonResponseDto> getLessonResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/lesson/" + lessonId, LessonResponseDto.class);

        assertEquals(HttpStatus.OK, getLessonResponse.getStatusCode());
        assertNotNull(getLessonResponse.getBody());
        assertEquals(lessonId, getLessonResponse.getBody().getId());
        assertEquals("Introduction to Java", getLessonResponse.getBody().getTitle());

        // Проверяем получение всех модулей по курсу
        ResponseEntity<ModuleResponseDto[]> getModulesResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/module/course/" + course.getId(), ModuleResponseDto[].class);

        assertEquals(HttpStatus.OK, getModulesResponse.getStatusCode());
        assertNotNull(getModulesResponse.getBody());
        assertTrue(getModulesResponse.getBody().length >= 1);
        
        boolean moduleFound = false;
        for (ModuleResponseDto moduleDto : getModulesResponse.getBody()) {
            if (moduleDto.getId().equals(moduleId)) {
                moduleFound = true;
                assertEquals("Java Fundamentals", moduleDto.getTitle());
                break;
            }
        }
        assertTrue(moduleFound, "Created module should be found in the course modules list");

        // Проверяем получение всех уроков по модулю
        ResponseEntity<LessonResponseDto[]> getLessonsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/lesson/module/" + moduleId, LessonResponseDto[].class);

        assertEquals(HttpStatus.OK, getLessonsResponse.getStatusCode());
        assertNotNull(getLessonsResponse.getBody());
        assertTrue(getLessonsResponse.getBody().length >= 1);
        
        boolean lessonFound = false;
        for (LessonResponseDto lessonDto : getLessonsResponse.getBody()) {
            if (lessonDto.getId().equals(lessonId)) {
                lessonFound = true;
                assertEquals("Introduction to Java", lessonDto.getTitle());
                break;
            }
        }
        assertTrue(lessonFound, "Created lesson should be found in the module lessons list");
    }
}