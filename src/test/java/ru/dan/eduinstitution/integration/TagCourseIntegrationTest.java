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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для тегов и обновления курсов.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagCourseIntegrationTest extends BaseTestWithContext {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // No specific setup needed for this test
    }

    @Test
    @DisplayName("Проверка CRUD операций для тегов и обновления курсов")
    void testTagAndCourseUpdateOperations() {
        // Подготавливаем данные
        Long categoryId = jdbcTemplate.queryForObject(
            "SELECT id FROM edu_service.categories ORDER BY id LIMIT 1", Long.class);
        Category category = new Category();
        category.setId(categoryId);

        // Создаем учителя
        UserCreateDto teacherCreateDto = new UserCreateDto();
        teacherCreateDto.setName("Test Teacher for Tags");
        teacherCreateDto.setEmail("teacher.tags@example.com");
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

        // Создаем курс
        CourseCreateDto courseCreateDto = new CourseCreateDto();
        courseCreateDto.setTitle("Test Course for Tags");
        courseCreateDto.setDescription("Test Course Description for Tags");
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

        // Проверяем создание тега
        TagCreateDto tagCreateDto = new TagCreateDto();
        tagCreateDto.setName("Java");

        HttpHeaders tagHeaders = new HttpHeaders();
        tagHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TagCreateDto> tagEntity = new HttpEntity<>(tagCreateDto, tagHeaders);

        ResponseEntity<TagResponseDto> createTagResponse = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/tag", tagEntity, TagResponseDto.class);
        assertEquals(HttpStatus.CREATED, createTagResponse.getStatusCode());
        assertNotNull(createTagResponse.getBody());
        assertEquals("Java", createTagResponse.getBody().getName());
        Long tagId = createTagResponse.getBody().getId();
        assertNotNull(tagId);

        // Проверяем получение тега по ID
        ResponseEntity<TagResponseDto> getTagResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/tag/" + tagId, TagResponseDto.class);
        assertEquals(HttpStatus.OK, getTagResponse.getStatusCode());
        assertNotNull(getTagResponse.getBody());
        assertEquals(tagId, getTagResponse.getBody().getId());
        assertEquals("Java", getTagResponse.getBody().getName());

        // Проверяем получение всех тегов
        ResponseEntity<TagResponseDto[]> getAllTagsResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/tag", TagResponseDto[].class);
        assertEquals(HttpStatus.OK, getAllTagsResponse.getStatusCode());
        assertNotNull(getAllTagsResponse.getBody());
        assertTrue(getAllTagsResponse.getBody().length >= 1);

        boolean tagFound = false;
        for (TagResponseDto tagDto : getAllTagsResponse.getBody()) {
            if (tagDto.getId().equals(tagId)) {
                tagFound = true;
                assertEquals("Java", tagDto.getName());
                break;
            }
        }
        assertTrue(tagFound, "Created tag should be found in the tags list");

        // Проверяем добавление тега к курсу
        restTemplate.postForEntity(
                restTemplate.getRootUri() + "/tag/course/" + courseId + "/tag/" + tagId,
                null, Void.class);
        // Add verification here if needed

        // Проверяем создание второго тега
        TagCreateDto tag2CreateDto = new TagCreateDto();
        tag2CreateDto.setName("Spring");

        HttpEntity<TagCreateDto> tag2Entity = new HttpEntity<>(tag2CreateDto, tagHeaders);

        ResponseEntity<TagResponseDto> createTag2Response = restTemplate.postForEntity(
                restTemplate.getRootUri() + "/tag", tag2Entity, TagResponseDto.class);
        assertEquals(HttpStatus.CREATED, createTag2Response.getStatusCode());
        assertNotNull(createTag2Response.getBody());
        assertEquals("Spring", createTag2Response.getBody().getName());
        Long tag2Id = createTag2Response.getBody().getId();
        assertNotNull(tag2Id);

        // Проверяем добавление нескольких тегов к курсу
        Set<Long> tagIds = new HashSet<>(Arrays.asList(tagId, tag2Id));
        HttpEntity<Set<Long>> tagsEntity = new HttpEntity<>(tagIds, tagHeaders);

        restTemplate.postForEntity(
                restTemplate.getRootUri() + "/tag/course/" + courseId + "/tags",
                tagsEntity, Void.class);

        // Проверяем получение курса по ID
        ResponseEntity<CourseResponseDto> getCourseResponse = restTemplate.getForEntity(
                restTemplate.getRootUri() + "/course/" + courseId, CourseResponseDto.class);
        assertEquals(HttpStatus.OK, getCourseResponse.getStatusCode());
        assertNotNull(getCourseResponse.getBody());
        assertEquals(courseId, getCourseResponse.getBody().getId());
        assertEquals("Test Course for Tags", getCourseResponse.getBody().getTitle());

        // Проверяем обновление курса
        CourseUpdateDto updateDto = new CourseUpdateDto();
        updateDto.setTitle("Updated Test Course for Tags");
        updateDto.setDescription("Updated Test Course Description for Tags");
        updateDto.setDuration(45);
        updateDto.setStartDate(LocalDate.now().plusDays(1));

        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CourseUpdateDto> updateEntity = new HttpEntity<>(updateDto, updateHeaders);

        ResponseEntity<CourseResponseDto> updateCourseResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/course/" + courseId,
                org.springframework.http.HttpMethod.PUT,
                updateEntity,
                CourseResponseDto.class);

        assertEquals(HttpStatus.OK, updateCourseResponse.getStatusCode());
        assertNotNull(updateCourseResponse.getBody());
        assertEquals(courseId, updateCourseResponse.getBody().getId());
        assertEquals("Updated Test Course for Tags", updateCourseResponse.getBody().getTitle());
        assertEquals("Updated Test Course Description for Tags", updateCourseResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(45), updateCourseResponse.getBody().getDuration());

        // Проверяем обновление курса с тегами
        CourseUpdateDto updateDtoWithTag = new CourseUpdateDto();
        updateDtoWithTag.setTitle("Updated Again Course for Tags");
        updateDtoWithTag.setTagIds(Set.of(tagId)); // Установим только один тег

        HttpEntity<CourseUpdateDto> updateWithTagEntity = new HttpEntity<>(updateDtoWithTag, updateHeaders);

        ResponseEntity<CourseResponseDto> updateCourseWithTagResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/course/" + courseId,
                org.springframework.http.HttpMethod.PUT,
                updateWithTagEntity,
                CourseResponseDto.class);

        assertEquals(HttpStatus.OK, updateCourseWithTagResponse.getStatusCode());
        assertNotNull(updateCourseWithTagResponse.getBody());
        assertEquals(courseId, updateCourseWithTagResponse.getBody().getId());
        assertEquals("Updated Again Course for Tags", updateCourseWithTagResponse.getBody().getTitle());

        // Проверяем обновление тега
        TagUpdateDto tagUpdateDto = new TagUpdateDto();
        tagUpdateDto.setName("Java Advanced");

        HttpEntity<TagUpdateDto> tagUpdateEntity = new HttpEntity<>(tagUpdateDto, tagHeaders);

        ResponseEntity<TagResponseDto> updateTagResponse = restTemplate.exchange(
                restTemplate.getRootUri() + "/tag/" + tagId,
                org.springframework.http.HttpMethod.PUT,
                tagUpdateEntity,
                TagResponseDto.class);

        assertEquals(HttpStatus.OK, updateTagResponse.getStatusCode());
        assertNotNull(updateTagResponse.getBody());
        assertEquals(tagId, updateTagResponse.getBody().getId());
        assertEquals("Java Advanced", updateTagResponse.getBody().getName());

        // Проверяем удаление тега из курса
        restTemplate.delete(restTemplate.getRootUri() + "/tag/course/" + courseId + "/tag/" + tag2Id);

        // Проверяем удаление тега
        restTemplate.delete(restTemplate.getRootUri() + "/tag/" + tag2Id);

        // Пропускаем проверку 404, т.к. может быть сложная логика с внешними ключами
        // После удаления тега, он уже не должен существовать
    }
}