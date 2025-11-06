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
import ru.dan.eduinstitution.model.*;
import ru.dan.eduinstitution.model.UserCreateDto;
import ru.dan.eduinstitution.model.UserResponseDto;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для тестов, вопросов, вариантов ответов и результатов тестов.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuizIntegrationTest extends BaseTestWithContext {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // No specific setup needed for this test
    }

    @Test
    @DisplayName("Проверка CRUD операций для тестов, вопросов, вариантов ответов и результатов тестов")
    void testQuizQuestionAnswerOptionQuizSubmissionCrudOperations() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);
        Category category = new Category();
        category.setId(categoryId);

        UserCreateDto teacherCreateDto = new UserCreateDto();
        teacherCreateDto.setName("Test Teacher for Quiz");
        teacherCreateDto.setEmail("teacher.quiz@example.com");
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

        UserCreateDto studentCreateDto = new UserCreateDto();
        studentCreateDto.setName("Test Student for Quiz");
        studentCreateDto.setEmail("student.quiz@example.com");
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
        courseCreateDto.setTitle("Test Course for Quiz");
        courseCreateDto.setDescription("Test Course Description for Quiz");
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

        // Создаем модуль
        ModuleCreateDto moduleCreateDto = new ModuleCreateDto();
        moduleCreateDto.setCourseId(courseId);
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

        // Проверяем создание теста
        QuizCreateDto quizCreateDto = new QuizCreateDto();
        quizCreateDto.setModuleId(moduleId);
        quizCreateDto.setTitle("Java Fundamentals Quiz");
        quizCreateDto.setTimeLimit(60);

        HttpHeaders quizHeaders = new HttpHeaders();
        quizHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<QuizCreateDto> quizEntity = new HttpEntity<>(quizCreateDto, quizHeaders);

        ResponseEntity<QuizResponseDto> createQuizResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/quiz", quizEntity, QuizResponseDto.class);
        assertEquals(HttpStatus.CREATED, createQuizResponse.getStatusCode());
        assertNotNull(createQuizResponse.getBody());
        assertEquals("Java Fundamentals Quiz", createQuizResponse.getBody().getTitle());
        assertEquals(moduleId, createQuizResponse.getBody().getModuleId());
        assertEquals("Java Fundamentals Module", createQuizResponse.getBody().getModuleTitle());
        assertEquals(Integer.valueOf(60), createQuizResponse.getBody().getTimeLimit());

        Long quizId = createQuizResponse.getBody().getId();
        assertNotNull(quizId);

        // Проверяем получение теста по ID
        ResponseEntity<QuizResponseDto> getQuizResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/quiz/" + quizId, QuizResponseDto.class);
        assertEquals(HttpStatus.OK, getQuizResponse.getStatusCode());
        assertNotNull(getQuizResponse.getBody());
        assertEquals(quizId, getQuizResponse.getBody().getId());
        assertEquals("Java Fundamentals Quiz", getQuizResponse.getBody().getTitle());

        // Проверяем получение теста по ID модуля
        ResponseEntity<QuizResponseDto> getQuizByModuleResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/quiz/module/" + moduleId, QuizResponseDto.class);
        assertEquals(HttpStatus.OK, getQuizByModuleResponse.getStatusCode());
        assertNotNull(getQuizByModuleResponse.getBody());
        assertEquals(quizId, getQuizByModuleResponse.getBody().getId());
        assertEquals("Java Fundamentals Quiz", getQuizByModuleResponse.getBody().getTitle());

        // Проверяем создание вопроса
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setQuizId(quizId);
        questionCreateDto.setText("What is the capital of France?");
        questionCreateDto.setType("SINGLE_CHOICE");

        HttpHeaders questionHeaders = new HttpHeaders();
        questionHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<QuestionCreateDto> questionEntity = new HttpEntity<>(questionCreateDto, questionHeaders);

        ResponseEntity<QuestionResponseDto> createQuestionResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/question", questionEntity, QuestionResponseDto.class);
        assertEquals(HttpStatus.CREATED, createQuestionResponse.getStatusCode());
        assertNotNull(createQuestionResponse.getBody());
        assertEquals("What is the capital of France?", createQuestionResponse.getBody().getText());
        assertEquals("SINGLE_CHOICE", createQuestionResponse.getBody().getType());
        assertEquals(quizId, createQuestionResponse.getBody().getQuizId());
        assertEquals("Java Fundamentals Quiz", createQuestionResponse.getBody().getQuizTitle());

        Long questionId = createQuestionResponse.getBody().getId();
        assertNotNull(questionId);

        // Проверяем получение вопроса по ID
        ResponseEntity<QuestionResponseDto> getQuestionResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/question/" + questionId, QuestionResponseDto.class);
        assertEquals(HttpStatus.OK, getQuestionResponse.getStatusCode());
        assertNotNull(getQuestionResponse.getBody());
        assertEquals(questionId, getQuestionResponse.getBody().getId());
        assertEquals("What is the capital of France?", getQuestionResponse.getBody().getText());

        // Проверяем создание варианта ответа
        AnswerOptionCreateDto answerOptionCreateDto = new AnswerOptionCreateDto();
        answerOptionCreateDto.setQuestionId(questionId);
        answerOptionCreateDto.setText("Paris");
        answerOptionCreateDto.setIsCorrect(true);

        HttpHeaders answerOptionHeaders = new HttpHeaders();
        answerOptionHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnswerOptionCreateDto> answerOptionEntity = new HttpEntity<>(answerOptionCreateDto, answerOptionHeaders);

        ResponseEntity<AnswerOptionResponseDto> createAnswerOptionResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/answer-option", answerOptionEntity, AnswerOptionResponseDto.class);
        assertEquals(HttpStatus.CREATED, createAnswerOptionResponse.getStatusCode());
        assertNotNull(createAnswerOptionResponse.getBody());
        assertEquals("Paris", createAnswerOptionResponse.getBody().getText());
        assertTrue(createAnswerOptionResponse.getBody().getIsCorrect());
        assertEquals(questionId, createAnswerOptionResponse.getBody().getQuestionId());
        assertEquals("What is the capital of France?", createAnswerOptionResponse.getBody().getQuestionText());

        Long answerOptionId = createAnswerOptionResponse.getBody().getId();
        assertNotNull(answerOptionId);

        // Проверяем получение варианта ответа по ID
        ResponseEntity<AnswerOptionResponseDto> getAnswerOptionResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/answer-option/" + answerOptionId, AnswerOptionResponseDto.class);
        assertEquals(HttpStatus.OK, getAnswerOptionResponse.getStatusCode());
        assertNotNull(getAnswerOptionResponse.getBody());
        assertEquals(answerOptionId, getAnswerOptionResponse.getBody().getId());
        assertEquals("Paris", getAnswerOptionResponse.getBody().getText());
        assertTrue(getAnswerOptionResponse.getBody().getIsCorrect());

        // Проверяем создание результата теста
        QuizSubmissionCreateDto quizSubmissionCreateDto = new QuizSubmissionCreateDto();
        quizSubmissionCreateDto.setQuizId(quizId);
        quizSubmissionCreateDto.setStudentId(studentId);
        quizSubmissionCreateDto.setScore(85);

        HttpHeaders quizSubmissionHeaders = new HttpHeaders();
        quizSubmissionHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<QuizSubmissionCreateDto> quizSubmissionEntity = new HttpEntity<>(quizSubmissionCreateDto, quizSubmissionHeaders);

        ResponseEntity<QuizSubmissionResponseDto> createQuizSubmissionResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/quiz-submission", quizSubmissionEntity, QuizSubmissionResponseDto.class);
        assertEquals(HttpStatus.CREATED, createQuizSubmissionResponse.getStatusCode());
        assertNotNull(createQuizSubmissionResponse.getBody());
        assertEquals(Integer.valueOf(85), createQuizSubmissionResponse.getBody().getScore());
        assertEquals(quizId, createQuizSubmissionResponse.getBody().getQuizId());
        assertEquals("Java Fundamentals Quiz", createQuizSubmissionResponse.getBody().getQuizTitle());
        assertEquals(studentId, createQuizSubmissionResponse.getBody().getStudentId());
        assertEquals("Test Student for Quiz", createQuizSubmissionResponse.getBody().getStudentName());
        assertNotNull(createQuizSubmissionResponse.getBody().getTakenAt());

        Long quizSubmissionId = createQuizSubmissionResponse.getBody().getId();
        assertNotNull(quizSubmissionId);

        // Проверяем получение результата теста по ID
        ResponseEntity<QuizSubmissionResponseDto> getQuizSubmissionResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/quiz-submission/" + quizSubmissionId, QuizSubmissionResponseDto.class);
        assertEquals(HttpStatus.OK, getQuizSubmissionResponse.getStatusCode());
        assertNotNull(getQuizSubmissionResponse.getBody());
        assertEquals(quizSubmissionId, getQuizSubmissionResponse.getBody().getId());
        assertEquals(Integer.valueOf(85), getQuizSubmissionResponse.getBody().getScore());
        assertEquals(studentId, getQuizSubmissionResponse.getBody().getStudentId());

        // Проверяем получение всех вопросов по ID теста
        ResponseEntity<QuestionResponseDto[]> getQuestionsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/question/quiz/" + quizId, QuestionResponseDto[].class);
        assertEquals(HttpStatus.OK, getQuestionsResponse.getStatusCode());
        assertNotNull(getQuestionsResponse.getBody());
        assertTrue(getQuestionsResponse.getBody().length >= 1);
        
        boolean questionFound = false;
        for (QuestionResponseDto questionDto : getQuestionsResponse.getBody()) {
            if (questionDto.getId().equals(questionId)) {
                questionFound = true;
                assertEquals("What is the capital of France?", questionDto.getText());
                break;
            }
        }
        assertTrue(questionFound, "Created question should be found in the quiz questions list");

        // Проверяем получение всех вариантов ответов по ID вопроса
        ResponseEntity<AnswerOptionResponseDto[]> getAnswerOptionsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/answer-option/question/" + questionId, AnswerOptionResponseDto[].class);
        assertEquals(HttpStatus.OK, getAnswerOptionsResponse.getStatusCode());
        assertNotNull(getAnswerOptionsResponse.getBody());
        assertTrue(getAnswerOptionsResponse.getBody().length >= 1);
        
        boolean answerOptionFound = false;
        for (AnswerOptionResponseDto answerOptionDto : getAnswerOptionsResponse.getBody()) {
            if (answerOptionDto.getId().equals(answerOptionId)) {
                answerOptionFound = true;
                assertEquals("Paris", answerOptionDto.getText());
                assertTrue(answerOptionDto.getIsCorrect());
                break;
            }
        }
        assertTrue(answerOptionFound, "Created answer option should be found in the question answer options list");

        // Проверяем получение всех результатов тестов по ID студента
        ResponseEntity<QuizSubmissionResponseDto[]> getQuizSubmissionsByStudentResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/quiz-submission/student/" + studentId, QuizSubmissionResponseDto[].class);
        assertEquals(HttpStatus.OK, getQuizSubmissionsByStudentResponse.getStatusCode());
        assertNotNull(getQuizSubmissionsByStudentResponse.getBody());
        assertTrue(getQuizSubmissionsByStudentResponse.getBody().length >= 1);
        
        boolean quizSubmissionFound = false;
        for (QuizSubmissionResponseDto quizSubmissionDto : getQuizSubmissionsByStudentResponse.getBody()) {
            if (quizSubmissionDto.getId().equals(quizSubmissionId)) {
                quizSubmissionFound = true;
                assertEquals(Integer.valueOf(85), quizSubmissionDto.getScore());
                assertEquals("Test Student for Quiz", quizSubmissionDto.getStudentName());
                break;
            }
        }
        assertTrue(quizSubmissionFound, "Created quiz submission should be found in the student quiz submissions list");

        // Проверяем получение всех результатов тестов по ID теста
        ResponseEntity<QuizSubmissionResponseDto[]> getQuizSubmissionsByQuizResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/quiz-submission/quiz/" + quizId, QuizSubmissionResponseDto[].class);
        assertEquals(HttpStatus.OK, getQuizSubmissionsByQuizResponse.getStatusCode());
        assertNotNull(getQuizSubmissionsByQuizResponse.getBody());
        assertTrue(getQuizSubmissionsByQuizResponse.getBody().length >= 1);
        
        boolean quizSubmissionByQuizFound = false;
        for (QuizSubmissionResponseDto quizSubmissionDto : getQuizSubmissionsByQuizResponse.getBody()) {
            if (quizSubmissionDto.getId().equals(quizSubmissionId)) {
                quizSubmissionByQuizFound = true;
                assertEquals(Integer.valueOf(85), quizSubmissionDto.getScore());
                assertEquals("Java Fundamentals Quiz", quizSubmissionDto.getQuizTitle());
                break;
            }
        }
        assertTrue(quizSubmissionByQuizFound, "Created quiz submission should be found in the quiz quiz submissions list");

        // Проверяем обновление результата теста
        QuizSubmissionUpdateDto updateDto = new QuizSubmissionUpdateDto();
        updateDto.setScore(90);

        HttpEntity<QuizSubmissionUpdateDto> updateEntity = new HttpEntity<>(updateDto, quizSubmissionHeaders);

        ResponseEntity<QuizSubmissionResponseDto> updateQuizSubmissionResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/quiz-submission/" + quizSubmissionId,
                org.springframework.http.HttpMethod.PUT,
                updateEntity,
                QuizSubmissionResponseDto.class);

        assertEquals(HttpStatus.OK, updateQuizSubmissionResponse.getStatusCode());
        assertNotNull(updateQuizSubmissionResponse.getBody());
        assertEquals(quizSubmissionId, updateQuizSubmissionResponse.getBody().getId());
        assertEquals(Integer.valueOf(90), updateQuizSubmissionResponse.getBody().getScore());
    }
}