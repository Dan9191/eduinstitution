package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.Tag;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.TagCreateDto;
import ru.dan.eduinstitution.model.TagResponseDto;
import ru.dan.eduinstitution.model.TagUpdateDto;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.TagRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для работы с тегами.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final CourseRepository courseRepository;

    /**
     * Создание тега.
     *
     * @param dto Данные для создания тега
     * @return Созданный тег
     */
    public TagResponseDto createTag(@Valid TagCreateDto dto) {
        log.info("Creating tag with name: {}", dto.getName());

        // Проверяем, что тег с таким именем не существует
        if (tagRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Tag with name '%s' already exists", dto.getName()));
        }

        Tag tag = new Tag();
        tag.setName(dto.getName());

        tag = tagRepository.save(tag);
        log.info("Tag created with ID: {}", tag.getId());

        return tagResponseDtoFromTag(tag);
    }

    /**
     * Получение тега по ID.
     *
     * @param id ID тега
     * @return Тег
     */
    public TagResponseDto getTagById(Long id) {
        log.info("Getting tag by ID: {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Tag with id '%s' not found", id)));

        return tagResponseDtoFromTag(tag);
    }

    /**
     * Обновление тега.
     *
     * @param id  ID тега
     * @param dto Данные для обновления тега
     * @return Обновленный тег
     */
    @Transactional
    public TagResponseDto updateTag(Long id, @Valid TagUpdateDto dto) {
        log.info("Updating tag with ID: {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Tag with id '%s' not found", id)));

        if (dto.getName() != null) {
            // Проверяем, что тег с новым именем не существует
            if (!dto.getName().equals(tag.getName())) {
                if (tagRepository.findByName(dto.getName()).isPresent()) {
                    throw new IllegalArgumentException(
                            String.format("Tag with name '%s' already exists", dto.getName()));
                }
            }
            tag.setName(dto.getName());
        }

        tag = tagRepository.save(tag);
        log.info("Tag updated with ID: {}", tag.getId());

        return tagResponseDtoFromTag(tag);
    }

    /**
     * Удаление тега.
     *
     * @param id ID тега
     */
    @Transactional
    public void deleteTag(Long id) {
        log.info("Deleting tag with ID: {}", id);

        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Tag with id '%s' not found", id));
        }

        tagRepository.deleteById(id);
        log.info("Tag deleted with ID: {}", id);
    }

    /**
     * Получение всех тегов.
     *
     * @return Список тегов
     */
    public List<TagResponseDto> getAllTags() {
        log.info("Getting all tags");

        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(this::tagResponseDtoFromTag)
                .collect(Collectors.toList());
    }

    /**
     * Получение тега по имени.
     *
     * @param name Имя тега
     * @return Тег
     */
    public TagResponseDto getTagByName(String name) {
        log.info("Getting tag by name: {}", name);

        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Tag with name '%s' not found", name)));

        return tagResponseDtoFromTag(tag);
    }

    /**
     * Назначение тега курсу.
     *
     * @param courseId ID курса
     * @param tagId ID тега
     * @return Обновленный курс с тегами
     */
    @Transactional
    public void addTagToCourse(Long courseId, Long tagId) {
        log.info("Adding tag ID {} to course ID {}", tagId, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course with id '%s' not found", courseId)));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Tag with id '%s' not found", tagId)));

        course.getTags().add(tag);
        courseRepository.save(course);

        log.info("Tag ID {} added to course ID {}", tagId, courseId);
    }

    /**
     * Удаление тега из курса.
     *
     * @param courseId ID курса
     * @param tagId ID тега
     * @return Обновленный курс с тегами
     */
    @Transactional
    public void removeTagFromCourse(Long courseId, Long tagId) {
        log.info("Removing tag ID {} from course ID {}", tagId, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course with id '%s' not found", courseId)));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Tag with id '%s' not found", tagId)));

        course.getTags().remove(tag);
        courseRepository.save(course);

        log.info("Tag ID {} removed from course ID {}", tagId, courseId);
    }

    /**
     * Назначение нескольких тегов курсу.
     *
     * @param courseId ID курса
     * @param tagIds Список ID тегов
     */
    @Transactional
    public void addTagsToCourse(Long courseId, Set<Long> tagIds) {
        log.info("Adding tags {} to course ID {}", tagIds, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course with id '%s' not found", courseId)));

        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Tag with id '%s' not found", tagId)));
            course.getTags().add(tag);
        }

        courseRepository.save(course);
        log.info("Tags {} added to course ID {}", tagIds, courseId);
    }

    /**
     * Удаление нескольких тегов из курса.
     *
     * @param courseId ID курса
     * @param tagIds Список ID тегов
     */
    @Transactional
    public void removeTagsFromCourse(Long courseId, Set<Long> tagIds) {
        log.info("Removing tags {} from course ID {}", tagIds, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course with id '%s' not found", courseId)));

        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Tag with id '%s' not found", tagId)));
            course.getTags().remove(tag);
        }

        courseRepository.save(course);
        log.info("Tags {} removed from course ID {}", tagIds, courseId);
    }

    private TagResponseDto tagResponseDtoFromTag(Tag tag) {
        TagResponseDto dto = new TagResponseDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}