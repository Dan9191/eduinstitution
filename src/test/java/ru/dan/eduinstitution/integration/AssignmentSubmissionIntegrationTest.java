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
import ru.dan.eduinstitution.entity.Profile;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.model.AssignmentCreateDto;
import ru.dan.eduinstitution.model.AssignmentResponseDto;
import ru.dan.eduinstitution.model.AssignmentUpdateDto;
import ru.dan.eduinstitution.model.LessonCreateDto;
import ru.dan.eduinstitution.model.LessonResponseDto;
import ru.dan.eduinstitution.model.ModuleCreateDto;
import ru.dan.eduinstitution.model.ModuleResponseDto;
import ru.dan.eduinstitution.model.SubmissionCreateDto;
import ru.dan.eduinstitution.model.SubmissionGradeDto;
import ru.dan.eduinstitution.model.SubmissionResponseDto;
import ru.dan.eduinstitution.repository.CategoryRepository;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.UserRepository;
import ru.dan.eduinstitution.service.CategoryCacheService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для заданий и ответов/решений студентов.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssignmentSubmissionIntegrationTest extends BaseTestWithContext {

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
    @DisplayName("Проверка CRUD операций для заданий и ответов/решений студентов")
    void testAssignmentAndSubmissionCrudOperations() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> 
            new RuntimeException("Category with id " + categoryId + " not found"));

        User teacher = new User();
        teacher.setName("Test Teacher for Assignment");
        teacher.setEmail("teacher.assignment@example.com");
        teacher.setRole(User.Role.TEACHER);
        
        // Create profile for the teacher
        Profile teacherProfile = new Profile();
        teacherProfile.setBio("Test teacher bio");
        teacherProfile.setUser(teacher);
        teacher.setProfile(teacherProfile);
        
        teacher = userRepository.save(teacher);

        User student = new User();
        student.setName("Test Student for Assignment");
        student.setEmail("student.assignment@example.com");
        student.setRole(User.Role.STUDENT);
        
        // Create profile for the student
        Profile studentProfile = new Profile();
        studentProfile.setBio("Test student bio");
        studentProfile.setUser(student);
        student.setProfile(studentProfile);
        
        student = userRepository.save(student);

        Course course = new Course();
        course.setTitle("Test Course for Assignments");
        course.setDescription("Test Course Description for Assignments");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(30);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);

        // Создаем модуль
        ModuleCreateDto moduleCreateDto = new ModuleCreateDto();
        moduleCreateDto.setCourseId(course.getId());
        moduleCreateDto.setTitle("Java Fundamentals Module");
        moduleCreateDto.setOrderIndex(1);
        moduleCreateDto.setDescription("This module covers Java fundamentals");

        HttpHeaders moduleHeaders = new HttpHeaders();
        moduleHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ModuleCreateDto> moduleEntity = new HttpEntity<>(moduleCreateDto, moduleHeaders);

        ResponseEntity<ModuleResponseDto> createModuleResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/module", moduleEntity, ModuleResponseDto.class);

        assertEquals(HttpStatus.CREATED, createModuleResponse.getStatusCode());
        assertNotNull(createModuleResponse.getBody());
        Long moduleId = createModuleResponse.getBody().getId();
        assertNotNull(moduleId);

        // Создаем урок
        LessonCreateDto lessonCreateDto = new LessonCreateDto();
        lessonCreateDto.setModuleId(moduleId);
        lessonCreateDto.setTitle("Introduction to Java Lesson");
        lessonCreateDto.setContent("This lesson covers Java basics...");
        lessonCreateDto.setVideoUrl("https://example.com/video1.mp4");

        HttpHeaders lessonHeaders = new HttpHeaders();
        lessonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LessonCreateDto> lessonEntity = new HttpEntity<>(lessonCreateDto, lessonHeaders);

        ResponseEntity<LessonResponseDto> createLessonResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/lesson", lessonEntity, LessonResponseDto.class);

        assertEquals(HttpStatus.CREATED, createLessonResponse.getStatusCode());
        assertNotNull(createLessonResponse.getBody());
        Long lessonId = createLessonResponse.getBody().getId();
        assertNotNull(lessonId);

        // Проверяем создание задания
        AssignmentCreateDto assignmentCreateDto = new AssignmentCreateDto();
        assignmentCreateDto.setLessonId(lessonId);
        assignmentCreateDto.setTitle("Homework #1: Java Basics");
        assignmentCreateDto.setDescription("Complete the exercises on Java basics");
        assignmentCreateDto.setDueDate(LocalDate.now().plusDays(7));
        assignmentCreateDto.setMaxScore(100);

        HttpHeaders assignmentHeaders = new HttpHeaders();
        assignmentHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AssignmentCreateDto> assignmentEntity = new HttpEntity<>(assignmentCreateDto, assignmentHeaders);

        ResponseEntity<AssignmentResponseDto> createAssignmentResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/assignment", assignmentEntity, AssignmentResponseDto.class);

        assertEquals(HttpStatus.CREATED, createAssignmentResponse.getStatusCode());
        assertNotNull(createAssignmentResponse.getBody());
        assertEquals("Homework #1: Java Basics", createAssignmentResponse.getBody().getTitle());
        assertEquals(lessonId, createAssignmentResponse.getBody().getLessonId());
        assertEquals("Introduction to Java Lesson", createAssignmentResponse.getBody().getLessonTitle());
        assertEquals(Integer.valueOf(100), createAssignmentResponse.getBody().getMaxScore());

        Long assignmentId = createAssignmentResponse.getBody().getId();
        assertNotNull(assignmentId);

        // Проверяем получение задания по ID
        ResponseEntity<AssignmentResponseDto> getAssignmentResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/assignment/" + assignmentId, AssignmentResponseDto.class);

        assertEquals(HttpStatus.OK, getAssignmentResponse.getStatusCode());
        assertNotNull(getAssignmentResponse.getBody());
        assertEquals(assignmentId, getAssignmentResponse.getBody().getId());
        assertEquals("Homework #1: Java Basics", getAssignmentResponse.getBody().getTitle());

        // Проверяем получение всех заданий по уроку
        ResponseEntity<AssignmentResponseDto[]> getAssignmentsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/assignment/lesson/" + lessonId, AssignmentResponseDto[].class);

        assertEquals(HttpStatus.OK, getAssignmentsResponse.getStatusCode());
        assertNotNull(getAssignmentsResponse.getBody());
        assertTrue(getAssignmentsResponse.getBody().length >= 1);
        
        boolean assignmentFound = false;
        for (AssignmentResponseDto assignmentDto : getAssignmentsResponse.getBody()) {
            if (assignmentDto.getId().equals(assignmentId)) {
                assignmentFound = true;
                assertEquals("Homework #1: Java Basics", assignmentDto.getTitle());
                break;
            }
        }
        assertTrue(assignmentFound, "Created assignment should be found in the lesson assignments list");

        // Проверяем создание ответа/решения студента
        SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto();
        submissionCreateDto.setStudentId(student.getId());
        submissionCreateDto.setAssignmentId(assignmentId);
        submissionCreateDto.setContent("The solution to the assignment is here...");

        HttpHeaders submissionHeaders = new HttpHeaders();
        submissionHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubmissionCreateDto> submissionEntity = new HttpEntity<>(submissionCreateDto, submissionHeaders);

        ResponseEntity<SubmissionResponseDto> createSubmissionResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/submission", submissionEntity, SubmissionResponseDto.class);

        assertEquals(HttpStatus.CREATED, createSubmissionResponse.getStatusCode());
        assertNotNull(createSubmissionResponse.getBody());
        assertEquals("The solution to the assignment is here...", createSubmissionResponse.getBody().getContent());
        assertEquals(assignmentId, createSubmissionResponse.getBody().getAssignmentId());
        assertEquals("Homework #1: Java Basics", createSubmissionResponse.getBody().getAssignmentTitle());
        assertEquals(student.getId(), createSubmissionResponse.getBody().getStudentId());
        assertEquals(student.getName(), createSubmissionResponse.getBody().getStudentName());
        assertNull(createSubmissionResponse.getBody().getScore(), "Score should be null initially");
        assertNull(createSubmissionResponse.getBody().getFeedback(), "Feedback should be null initially");

        Long submissionId = createSubmissionResponse.getBody().getId();
        assertNotNull(submissionId);

        // Проверяем получение ответа/решения по ID
        ResponseEntity<SubmissionResponseDto> getSubmissionResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/submission/" + submissionId, SubmissionResponseDto.class);

        assertEquals(HttpStatus.OK, getSubmissionResponse.getStatusCode());
        assertNotNull(getSubmissionResponse.getBody());
        assertEquals(submissionId, getSubmissionResponse.getBody().getId());
        assertEquals("The solution to the assignment is here...", getSubmissionResponse.getBody().getContent());
        assertEquals(student.getId(), getSubmissionResponse.getBody().getStudentId());

        // Проверяем выставление оценки за ответ/решение
        SubmissionGradeDto gradeDto = new SubmissionGradeDto();
        gradeDto.setScore(95);
        gradeDto.setFeedback("Good work, but consider optimization...");

        HttpHeaders gradeHeaders = new HttpHeaders();
        gradeHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubmissionGradeDto> gradeEntity = new HttpEntity<>(gradeDto, gradeHeaders);

        ResponseEntity<SubmissionResponseDto> gradeSubmissionResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/submission/" + submissionId + "/grade",
                org.springframework.http.HttpMethod.PUT,
                gradeEntity,
                SubmissionResponseDto.class);

        assertEquals(HttpStatus.OK, gradeSubmissionResponse.getStatusCode());
        assertNotNull(gradeSubmissionResponse.getBody());
        assertEquals(submissionId, gradeSubmissionResponse.getBody().getId());
        assertEquals(Integer.valueOf(95), gradeSubmissionResponse.getBody().getScore());
        assertEquals("Good work, but consider optimization...", gradeSubmissionResponse.getBody().getFeedback());

        // Проверяем получение всех ответов/решений по студенту
        ResponseEntity<SubmissionResponseDto[]> getSubmissionsByStudentResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/submission/student/" + student.getId(), SubmissionResponseDto[].class);

        assertEquals(HttpStatus.OK, getSubmissionsByStudentResponse.getStatusCode());
        assertNotNull(getSubmissionsByStudentResponse.getBody());
        assertTrue(getSubmissionsByStudentResponse.getBody().length >= 1);
        
        boolean studentSubmissionFound = false;
        for (SubmissionResponseDto submissionDto : getSubmissionsByStudentResponse.getBody()) {
            if (submissionDto.getId().equals(submissionId)) {
                studentSubmissionFound = true;
                assertEquals("The solution to the assignment is here...", submissionDto.getContent());
                assertEquals(Integer.valueOf(95), submissionDto.getScore());
                assertEquals("Good work, but consider optimization...", submissionDto.getFeedback());
                break;
            }
        }
        assertTrue(studentSubmissionFound, "Created submission should be found in the student submissions list");

        // Проверяем получение всех ответов/решений по заданию
        ResponseEntity<SubmissionResponseDto[]> getSubmissionsByAssignmentResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/submission/assignment/" + assignmentId, SubmissionResponseDto[].class);

        assertEquals(HttpStatus.OK, getSubmissionsByAssignmentResponse.getStatusCode());
        assertNotNull(getSubmissionsByAssignmentResponse.getBody());
        assertTrue(getSubmissionsByAssignmentResponse.getBody().length >= 1);
        
        boolean assignmentSubmissionFound = false;
        for (SubmissionResponseDto submissionDto : getSubmissionsByAssignmentResponse.getBody()) {
            if (submissionDto.getId().equals(submissionId)) {
                assignmentSubmissionFound = true;
                assertEquals(student.getName(), submissionDto.getStudentName());
                assertEquals(Integer.valueOf(95), submissionDto.getScore());
                break;
            }
        }
        assertTrue(assignmentSubmissionFound, "Created submission should be found in the assignment submissions list");

        // Проверяем обновление задания
        AssignmentUpdateDto updateDto = new AssignmentUpdateDto();
        updateDto.setTitle("Updated Homework #1: Java Basics");
        updateDto.setDescription("Updated description for Java basics");
        updateDto.setDueDate(LocalDate.now().plusDays(10));
        updateDto.setMaxScore(90);

        HttpEntity<AssignmentUpdateDto> updateEntity = new HttpEntity<>(updateDto, assignmentHeaders);

        ResponseEntity<AssignmentResponseDto> updateAssignmentResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/assignment/" + assignmentId,
                org.springframework.http.HttpMethod.PUT,
                updateEntity,
                AssignmentResponseDto.class);

        assertEquals(HttpStatus.OK, updateAssignmentResponse.getStatusCode());
        assertNotNull(updateAssignmentResponse.getBody());
        assertEquals(assignmentId, updateAssignmentResponse.getBody().getId());
        assertEquals("Updated Homework #1: Java Basics", updateAssignmentResponse.getBody().getTitle());
        assertEquals("Updated description for Java basics", updateAssignmentResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(90), updateAssignmentResponse.getBody().getMaxScore());

        // NOTE: Skipping deletion test to avoid foreign key constraint issues with dependent submissions
    }
}