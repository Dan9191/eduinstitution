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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для расширенного управления модулями и записями на курсы.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdvancedModuleEnrollmentIntegrationTest extends BaseTestWithContext {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // No specific setup needed for this test
    }

    @Test
    @DisplayName("Проверка расширенного управления модулями и записями на курсы")
    void testAdvancedModuleEnrollmentManagement() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);

        // Создаем преподавателя
        UserCreateDto teacherCreateDto = new UserCreateDto();
        teacherCreateDto.setName("Test Teacher for Advanced Management");
        teacherCreateDto.setEmail("teacher.advanced@example.com");
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

        // Создаем первого студента
        UserCreateDto student1CreateDto = new UserCreateDto();
        student1CreateDto.setName("Test Student 1 for Advanced Management");
        student1CreateDto.setEmail("student1.advanced@example.com");
        student1CreateDto.setRole("STUDENT");
        student1CreateDto.setBio("Test student 1 bio");

        HttpEntity<UserCreateDto> student1Entity = new HttpEntity<>(student1CreateDto, userHeaders);

        ResponseEntity<UserResponseDto> createStudent1Response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", student1Entity, UserResponseDto.class);
        assertEquals(HttpStatus.CREATED, createStudent1Response.getStatusCode());
        Long student1Id = createStudent1Response.getBody().getId();
        assertNotNull(student1Id);

        // Создаем второго студента
        UserCreateDto student2CreateDto = new UserCreateDto();
        student2CreateDto.setName("Test Student 2 for Advanced Management");
        student2CreateDto.setEmail("student2.advanced@example.com");
        student2CreateDto.setRole("STUDENT");
        student2CreateDto.setBio("Test student 2 bio");

        HttpEntity<UserCreateDto> student2Entity = new HttpEntity<>(student2CreateDto, userHeaders);

        ResponseEntity<UserResponseDto> createStudent2Response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/user/create", student2Entity, UserResponseDto.class);
        assertEquals(HttpStatus.CREATED, createStudent2Response.getStatusCode());
        Long student2Id = createStudent2Response.getBody().getId();
        assertNotNull(student2Id);

        // Создаем первый курс
        CourseCreateDto course1CreateDto = new CourseCreateDto();
        course1CreateDto.setTitle("Test Course 1 for Advanced Management");
        course1CreateDto.setDescription("Test Course 1 Description for Advanced Management");
        course1CreateDto.setCategoryId(categoryId);
        course1CreateDto.setTeacherId(teacherId);
        course1CreateDto.setDuration(30);
        course1CreateDto.setStartDate(LocalDate.now());

        HttpHeaders courseHeaders = new HttpHeaders();
        courseHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CourseCreateDto> course1Entity = new HttpEntity<>(course1CreateDto, courseHeaders);

        ResponseEntity<CourseResponseDto> createCourse1Response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/course/create", course1Entity, CourseResponseDto.class);
        assertEquals(HttpStatus.CREATED, createCourse1Response.getStatusCode());
        assertNotNull(createCourse1Response.getBody());
        Long course1Id = createCourse1Response.getBody().getId();
        assertNotNull(course1Id);

        // Создаем второй курс
        CourseCreateDto course2CreateDto = new CourseCreateDto();
        course2CreateDto.setTitle("Test Course 2 for Advanced Management");
        course2CreateDto.setDescription("Test Course 2 Description for Advanced Management");
        course2CreateDto.setCategoryId(categoryId);
        course2CreateDto.setTeacherId(teacherId);
        course2CreateDto.setDuration(45);
        course2CreateDto.setStartDate(LocalDate.now());

        HttpEntity<CourseCreateDto> course2Entity = new HttpEntity<>(course2CreateDto, courseHeaders);

        ResponseEntity<CourseResponseDto> createCourse2Response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/course/create", course2Entity, CourseResponseDto.class);
        assertEquals(HttpStatus.CREATED, createCourse2Response.getStatusCode());
        assertNotNull(createCourse2Response.getBody());
        Long course2Id = createCourse2Response.getBody().getId();
        assertNotNull(course2Id);

        // Создаем модуль для первого курса
        ModuleCreateDto moduleCreateDto = new ModuleCreateDto();
        moduleCreateDto.setCourseId(course1Id);
        moduleCreateDto.setTitle("Initial Module Title");
        moduleCreateDto.setOrderIndex(1);
        moduleCreateDto.setDescription("Initial module description");

        HttpHeaders moduleHeaders = new HttpHeaders();
        moduleHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ModuleCreateDto> moduleEntity = new HttpEntity<>(moduleCreateDto, moduleHeaders);

        ResponseEntity<ModuleResponseDto> createModuleResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/module", moduleEntity, ModuleResponseDto.class);
        assertEquals(HttpStatus.CREATED, createModuleResponse.getStatusCode());
        assertNotNull(createModuleResponse.getBody());
        assertEquals("Initial Module Title", createModuleResponse.getBody().getTitle());
        assertEquals(course1Id, createModuleResponse.getBody().getCourseId());
        assertEquals(1, createModuleResponse.getBody().getOrderIndex());
        Long moduleId = createModuleResponse.getBody().getId();
        assertNotNull(moduleId);

        // Проверяем перемещение модуля в другой курс
        ResponseEntity<ModuleResponseDto> moveModuleResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/module/" + moduleId + "/move-to-course/" + course2Id,
                org.springframework.http.HttpMethod.PUT,
                null,
                ModuleResponseDto.class);
        assertEquals(HttpStatus.OK, moveModuleResponse.getStatusCode());
        assertNotNull(moveModuleResponse.getBody());
        assertEquals(moduleId, moveModuleResponse.getBody().getId());
        assertEquals(course2Id, moveModuleResponse.getBody().getCourseId());
        assertEquals("Test Course 2 for Advanced Management", moveModuleResponse.getBody().getCourseTitle());

        // Проверяем изменение порядка модуля
        ResponseEntity<ModuleResponseDto> reorderModuleResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/module/" + moduleId + "/reorder",
                org.springframework.http.HttpMethod.PUT,
                new HttpEntity<>(3, moduleHeaders),
                ModuleResponseDto.class);
        assertEquals(HttpStatus.OK, reorderModuleResponse.getStatusCode());
        assertNotNull(reorderModuleResponse.getBody());
        assertEquals(moduleId, reorderModuleResponse.getBody().getId());
        assertEquals(Integer.valueOf(3), reorderModuleResponse.getBody().getOrderIndex());

        // Записываем студента на курс
        EnrollmentRequestDto enrollmentRequestDto = new EnrollmentRequestDto();
        enrollmentRequestDto.setStudentId(student1Id);
        enrollmentRequestDto.setCourseId(course1Id);

        HttpHeaders enrollmentHeaders = new HttpHeaders();
        enrollmentHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EnrollmentRequestDto> enrollmentEntity = new HttpEntity<>(enrollmentRequestDto, enrollmentHeaders);

        ResponseEntity<EnrollmentResponseDto> enrollResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/enrollment/enroll", enrollmentEntity, EnrollmentResponseDto.class);
        assertEquals(HttpStatus.CREATED, enrollResponse.getStatusCode());
        assertNotNull(enrollResponse.getBody());
        assertEquals(student1Id, enrollResponse.getBody().getStudentId());
        assertEquals(course1Id, enrollResponse.getBody().getCourseId());
        assertEquals("Active", enrollResponse.getBody().getStatus());

        // Проверяем обновление статуса записи
        ResponseEntity<EnrollmentResponseDto> updateStatusResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/enrollment/update-status/" + student1Id + "/" + course1Id,
                org.springframework.http.HttpMethod.PUT,
                new HttpEntity<>("Completed", enrollmentHeaders),
                EnrollmentResponseDto.class);
        assertEquals(HttpStatus.OK, updateStatusResponse.getStatusCode());
        assertNotNull(updateStatusResponse.getBody());
        assertEquals(student1Id, updateStatusResponse.getBody().getStudentId());
        assertEquals(course1Id, updateStatusResponse.getBody().getCourseId());
        assertEquals("Completed", updateStatusResponse.getBody().getStatus());

        // Проверяем получение записей по статусу
        ResponseEntity<EnrollmentResponseDto[]> getStatusEnrollmentsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/enrollment/by-status/Completed", EnrollmentResponseDto[].class);
        assertEquals(HttpStatus.OK, getStatusEnrollmentsResponse.getStatusCode());
        assertNotNull(getStatusEnrollmentsResponse.getBody());
        boolean found = false;
        for (EnrollmentResponseDto enr : getStatusEnrollmentsResponse.getBody()) {
            if (enr.getStudentId().equals(student1Id) && enr.getCourseId().equals(course1Id)) {
                found = true;
                assertEquals("Completed", enr.getStatus());
                break;
            }
        }
        assertTrue(found, "Completed enrollment should be found in the status query");

        // Проверяем получение записей студента
        ResponseEntity<EnrollmentResponseDto[]> getStudentEnrollmentsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/enrollment/student/" + student1Id, EnrollmentResponseDto[].class);
        assertEquals(HttpStatus.OK, getStudentEnrollmentsResponse.getStatusCode());
        assertNotNull(getStudentEnrollmentsResponse.getBody());
        assertTrue(getStudentEnrollmentsResponse.getBody().length >= 1);

        // Проверяем получение записей курса
        ResponseEntity<EnrollmentResponseDto[]> getCourseEnrollmentsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/enrollment/course/" + course1Id, EnrollmentResponseDto[].class);
        assertEquals(HttpStatus.OK, getCourseEnrollmentsResponse.getStatusCode());
        assertNotNull(getCourseEnrollmentsResponse.getBody());
        assertTrue(getCourseEnrollmentsResponse.getBody().length >= 1);

        // Проверяем отчисление студента
        restTemplate.delete(restTemplate.getRootUri() + "/enrollment/unenroll/" + student1Id + "/" + course1Id);

        // Проверяем, что студент больше не записан
        ResponseEntity<EnrollmentResponseDto[]> getStudentEnrollmentsAfterUnenrollResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/enrollment/student/" + student1Id, EnrollmentResponseDto[].class);
        assertEquals(HttpStatus.OK, getStudentEnrollmentsAfterUnenrollResponse.getStatusCode());
        assertNotNull(getStudentEnrollmentsAfterUnenrollResponse.getBody());
        
        boolean stillEnrolled = false;
        for (EnrollmentResponseDto enr : getStudentEnrollmentsAfterUnenrollResponse.getBody()) {
            if (enr.getCourseId().equals(course1Id) && "Active".equals(enr.getStatus())) {
                stillEnrolled = true;
                break;
            }
        }
        assertFalse(stillEnrolled, "Student should not be actively enrolled after unenrollment");
    }
}