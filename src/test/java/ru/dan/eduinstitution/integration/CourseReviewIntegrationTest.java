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
import ru.dan.eduinstitution.model.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для отзывов о курсах.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseReviewIntegrationTest extends BaseTestWithContext {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // No specific setup needed for this test
    }

    @Test
    @DisplayName("Проверка CRUD операций для отзывов о курсах")
    void testCourseReviewCrudOperations() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);

        // Создаем учителя
        UserCreateDto teacherCreateDto = new UserCreateDto();
        teacherCreateDto.setName("Test Teacher for Review");
        teacherCreateDto.setEmail("teacher.review@example.com");
        teacherCreateDto.setRole("TEACHER");
        teacherCreateDto.setBio("Test teacher bio");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreateDto> teacherEntity = new HttpEntity<>(teacherCreateDto, userHeaders);

        ResponseEntity<UserResponseDto> createTeacherResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", teacherEntity, UserResponseDto.class);
        assertEquals(HttpStatus.CREATED, createTeacherResponse.getStatusCode());
        Long teacherId = createTeacherResponse.getBody().getId();
        assertNotNull(teacherId);

        // Создаем студента
        UserCreateDto studentCreateDto = new UserCreateDto();
        studentCreateDto.setName("Test Student for Review");
        studentCreateDto.setEmail("student.review@example.com");
        studentCreateDto.setRole("STUDENT");
        studentCreateDto.setBio("Test student bio");

        HttpEntity<UserCreateDto> studentEntity = new HttpEntity<>(studentCreateDto, userHeaders);

        ResponseEntity<UserResponseDto> createStudentResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", studentEntity, UserResponseDto.class);
        assertEquals(HttpStatus.CREATED, createStudentResponse.getStatusCode());
        Long studentId = createStudentResponse.getBody().getId();
        assertNotNull(studentId);

        // Создаем курс
        CourseCreateDto courseCreateDto = new CourseCreateDto();
        courseCreateDto.setTitle("Test Course for Review");
        courseCreateDto.setDescription("Test Course Description for Review");
        courseCreateDto.setCategoryId(categoryId);
        courseCreateDto.setTeacherId(teacherId);
        courseCreateDto.setDuration(30);
        courseCreateDto.setStartDate(LocalDate.now());

        HttpHeaders courseHeaders = new HttpHeaders();
        courseHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CourseCreateDto> courseEntity = new HttpEntity<>(courseCreateDto, courseHeaders);

        ResponseEntity<CourseResponseDto> createCourseResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/course/create", courseEntity, CourseResponseDto.class);
        assertEquals(HttpStatus.CREATED, createCourseResponse.getStatusCode());
        assertNotNull(createCourseResponse.getBody());
        Long courseId = createCourseResponse.getBody().getId();
        assertNotNull(courseId);

        // Проверяем добавление отзыва о курсе (addReview method)
        CourseReviewCreateDto reviewCreateDto = new CourseReviewCreateDto();
        reviewCreateDto.setCourseId(courseId);
        reviewCreateDto.setStudentId(studentId);
        reviewCreateDto.setRating(5);
        reviewCreateDto.setComment("Great course with excellent content!");

        HttpHeaders reviewHeaders = new HttpHeaders();
        reviewHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CourseReviewCreateDto> reviewEntity = new HttpEntity<>(reviewCreateDto, reviewHeaders);

        ResponseEntity<CourseReviewResponseDto> createReviewResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/course-review", reviewEntity, CourseReviewResponseDto.class);
        assertEquals(HttpStatus.CREATED, createReviewResponse.getStatusCode());
        assertNotNull(createReviewResponse.getBody());
        assertEquals(Integer.valueOf(5), createReviewResponse.getBody().getRating());
        assertEquals("Great course with excellent content!", createReviewResponse.getBody().getComment());
        assertEquals(courseId, createReviewResponse.getBody().getCourseId());
        assertEquals(studentId, createReviewResponse.getBody().getStudentId());
        assertNotNull(createReviewResponse.getBody().getCreatedAt());

        Long reviewId = createReviewResponse.getBody().getId();
        assertNotNull(reviewId);

        // Проверяем получение отзыва по ID
        ResponseEntity<CourseReviewResponseDto> getReviewResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/course-review/" + reviewId, CourseReviewResponseDto.class);
        assertEquals(HttpStatus.OK, getReviewResponse.getStatusCode());
        assertNotNull(getReviewResponse.getBody());
        assertEquals(reviewId, getReviewResponse.getBody().getId());
        assertEquals(Integer.valueOf(5), getReviewResponse.getBody().getRating());
        assertEquals("Great course with excellent content!", getReviewResponse.getBody().getComment());

        // Проверяем получение всех отзывов по курсу (getReviewsByCourse method)
        ResponseEntity<CourseReviewResponseDto[]> getReviewsByCourseResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/course-review/course/" + courseId, CourseReviewResponseDto[].class);
        assertEquals(HttpStatus.OK, getReviewsByCourseResponse.getStatusCode());
        assertNotNull(getReviewsByCourseResponse.getBody());
        assertTrue(getReviewsByCourseResponse.getBody().length >= 1);
        
        boolean reviewFound = false;
        for (CourseReviewResponseDto reviewDto : getReviewsByCourseResponse.getBody()) {
            if (reviewDto.getId().equals(reviewId)) {
                reviewFound = true;
                assertEquals(Integer.valueOf(5), reviewDto.getRating());
                assertEquals("Great course with excellent content!", reviewDto.getComment());
                assertEquals(courseId, reviewDto.getCourseId());
                assertEquals(studentId, reviewDto.getStudentId());
                break;
            }
        }
        assertTrue(reviewFound, "Created review should be found in the course reviews list");

        // Проверяем получение всех отзывов по студенту
        ResponseEntity<CourseReviewResponseDto[]> getReviewsByStudentResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/course-review/student/" + studentId, CourseReviewResponseDto[].class);
        assertEquals(HttpStatus.OK, getReviewsByStudentResponse.getStatusCode());
        assertNotNull(getReviewsByStudentResponse.getBody());
        assertTrue(getReviewsByStudentResponse.getBody().length >= 1);
        
        boolean studentReviewFound = false;
        for (CourseReviewResponseDto reviewDto : getReviewsByStudentResponse.getBody()) {
            if (reviewDto.getId().equals(reviewId)) {
                studentReviewFound = true;
                assertEquals(Integer.valueOf(5), reviewDto.getRating());
                assertEquals("Great course with excellent content!", reviewDto.getComment());
                assertEquals(courseId, reviewDto.getCourseId());
                assertEquals(studentId, reviewDto.getStudentId());
                break;
            }
        }
        assertTrue(studentReviewFound, "Created review should be found in the student reviews list");

        // Проверяем получение среднего рейтинга по курсу
        ResponseEntity<Double> getAverageRatingResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/course-review/course/" + courseId + "/average-rating", Double.class);
        assertEquals(HttpStatus.OK, getAverageRatingResponse.getStatusCode());
        assertNotNull(getAverageRatingResponse.getBody());
        assertEquals(Double.valueOf(5.0), getAverageRatingResponse.getBody());

        // Проверяем обновление отзыва
        CourseReviewUpdateDto updateDto = new CourseReviewUpdateDto();
        updateDto.setRating(4);
        updateDto.setComment("Good course but could be improved.");

        HttpEntity<CourseReviewUpdateDto> updateEntity = new HttpEntity<>(updateDto, reviewHeaders);

        ResponseEntity<CourseReviewResponseDto> updateReviewResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/course-review/" + reviewId,
                org.springframework.http.HttpMethod.PUT,
                updateEntity,
                CourseReviewResponseDto.class);

        assertEquals(HttpStatus.OK, updateReviewResponse.getStatusCode());
        assertNotNull(updateReviewResponse.getBody());
        assertEquals(reviewId, updateReviewResponse.getBody().getId());
        assertEquals(Integer.valueOf(4), updateReviewResponse.getBody().getRating());
        assertEquals("Good course but could be improved.", updateReviewResponse.getBody().getComment());

        // Проверяем, что средний рейтинг изменился
        ResponseEntity<Double> getAverageRatingAfterUpdateResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/course-review/course/" + courseId + "/average-rating", Double.class);
        assertEquals(HttpStatus.OK, getAverageRatingAfterUpdateResponse.getStatusCode());
        assertNotNull(getAverageRatingAfterUpdateResponse.getBody());
        assertEquals(Double.valueOf(4.0), getAverageRatingAfterUpdateResponse.getBody());
    }

    @Test
    @DisplayName("Проверка ограничений при добавлении отзыва")
    void testCourseReviewConstraints() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);

        // Создаем учителя
        UserCreateDto teacherCreateDto = new UserCreateDto();
        teacherCreateDto.setName("Test Teacher Constraints");
        teacherCreateDto.setEmail("teacher.constraints@example.com");
        teacherCreateDto.setRole("TEACHER");
        teacherCreateDto.setBio("Test teacher bio");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreateDto> teacherEntity = new HttpEntity<>(teacherCreateDto, userHeaders);

        ResponseEntity<UserResponseDto> createTeacherResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", teacherEntity, UserResponseDto.class);
        assertEquals(HttpStatus.CREATED, createTeacherResponse.getStatusCode());
        Long teacherId = createTeacherResponse.getBody().getId();
        assertNotNull(teacherId);

        // Создаем студента 1
        UserCreateDto student1CreateDto = new UserCreateDto();
        student1CreateDto.setName("Test Student 1 for Constraints");
        student1CreateDto.setEmail("student1.constraints@example.com");
        student1CreateDto.setRole("STUDENT");
        student1CreateDto.setBio("Test student bio 1");

        HttpEntity<UserCreateDto> student1Entity = new HttpEntity<>(student1CreateDto, userHeaders);

        ResponseEntity<UserResponseDto> createStudent1Response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", student1Entity, UserResponseDto.class);
        assertEquals(HttpStatus.CREATED, createStudent1Response.getStatusCode());
        Long student1Id = createStudent1Response.getBody().getId();
        assertNotNull(student1Id);

        // Создаем студента 2
        UserCreateDto student2CreateDto = new UserCreateDto();
        student2CreateDto.setName("Test Student 2 for Constraints");
        student2CreateDto.setEmail("student2.constraints@example.com");
        student2CreateDto.setRole("STUDENT");
        student2CreateDto.setBio("Test student bio 2");

        HttpEntity<UserCreateDto> student2Entity = new HttpEntity<>(student2CreateDto, userHeaders);

        ResponseEntity<UserResponseDto> createStudent2Response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", student2Entity, UserResponseDto.class);
        assertEquals(HttpStatus.CREATED, createStudent2Response.getStatusCode());
        Long student2Id = createStudent2Response.getBody().getId();
        assertNotNull(student2Id);

        // Создаем курс
        CourseCreateDto courseCreateDto = new CourseCreateDto();
        courseCreateDto.setTitle("Test Course for Constraints");
        courseCreateDto.setDescription("Test Course Description for Constraints");
        courseCreateDto.setCategoryId(categoryId);
        courseCreateDto.setTeacherId(teacherId);
        courseCreateDto.setDuration(30);
        courseCreateDto.setStartDate(LocalDate.now());

        HttpHeaders courseHeaders = new HttpHeaders();
        courseHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CourseCreateDto> courseEntity = new HttpEntity<>(courseCreateDto, courseHeaders);

        ResponseEntity<CourseResponseDto> createCourseResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/course/create", courseEntity, CourseResponseDto.class);
        assertEquals(HttpStatus.CREATED, createCourseResponse.getStatusCode());
        assertNotNull(createCourseResponse.getBody());
        Long courseId = createCourseResponse.getBody().getId();
        assertNotNull(courseId);

        // Добавляем первый отзыв
        CourseReviewCreateDto reviewCreateDto = new CourseReviewCreateDto();
        reviewCreateDto.setCourseId(courseId);
        reviewCreateDto.setStudentId(student1Id);
        reviewCreateDto.setRating(5);
        reviewCreateDto.setComment("Great course!");

        HttpHeaders reviewHeaders = new HttpHeaders();
        reviewHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CourseReviewCreateDto> reviewEntity = new HttpEntity<>(reviewCreateDto, reviewHeaders);

        ResponseEntity<CourseReviewResponseDto> createFirstReviewResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/course-review", reviewEntity, CourseReviewResponseDto.class);
        assertEquals(HttpStatus.CREATED, createFirstReviewResponse.getStatusCode());
        assertNotNull(createFirstReviewResponse.getBody());
        Long firstReviewId = createFirstReviewResponse.getBody().getId();
        assertNotNull(firstReviewId);

        // Проверяем, что можно добавить отзыв от другого студента на тот же курс (это должно работать)
        CourseReviewCreateDto validReviewCreateDto = new CourseReviewCreateDto();
        validReviewCreateDto.setCourseId(courseId);
        validReviewCreateDto.setStudentId(student2Id);
        validReviewCreateDto.setRating(3);
        validReviewCreateDto.setComment("Decent course");

        HttpEntity<CourseReviewCreateDto> validReviewEntity = new HttpEntity<>(validReviewCreateDto, reviewHeaders);

        ResponseEntity<CourseReviewResponseDto> createValidReviewResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/course-review", validReviewEntity, CourseReviewResponseDto.class);
        assertEquals(HttpStatus.CREATED, createValidReviewResponse.getStatusCode());
        assertNotNull(createValidReviewResponse.getBody());
        Long validReviewId = createValidReviewResponse.getBody().getId();
        assertNotNull(validReviewId);

        // Проверяем, что средний рейтинг теперь усреднен для двух отзывов
        ResponseEntity<Double> getAverageRatingResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/course-review/course/" + courseId + "/average-rating", Double.class);
        assertEquals(HttpStatus.OK, getAverageRatingResponse.getStatusCode());
        assertNotNull(getAverageRatingResponse.getBody());
        // Среднее между 5 и 3 = 4.0 (approximately, depending on exact values)
        assertTrue(getAverageRatingResponse.getBody() >= 3.0 && getAverageRatingResponse.getBody() <= 5.0,
                  "Average rating should be between individual ratings");
    }
}