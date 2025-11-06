package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Lesson;
import ru.dan.eduinstitution.entity.Module;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.LessonCreateDto;
import ru.dan.eduinstitution.model.LessonResponseDto;
import ru.dan.eduinstitution.model.LessonUpdateDto;
import ru.dan.eduinstitution.repository.LessonRepository;
import ru.dan.eduinstitution.repository.ModuleRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с уроками.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    /**
     * Создание урока.
     *
     * @param dto Данные для создания урока
     * @return Созданный урок
     */
    @Transactional
    public LessonResponseDto createLesson(@Valid LessonCreateDto dto) {
        log.info("Creating lesson with title: {}", dto.getTitle());

        // Проверяем, что модуль существует
        Module module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Module with id '%s' not found", dto.getModuleId())));

        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(dto.getTitle());
        lesson.setContent(dto.getContent());
        lesson.setVideoUrl(dto.getVideoUrl());

        lesson = lessonRepository.save(lesson);
        log.info("Lesson created with ID: {}", lesson.getId());

        return lessonResponseDtoFromLesson(lesson);
    }

    /**
     * Получение урока по ID.
     *
     * @param id ID урока
     * @return Урок
     */
    public LessonResponseDto getLessonById(Long id) {
        log.info("Getting lesson by ID: {}", id);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Lesson with id '%s' not found", id)));

        return lessonResponseDtoFromLesson(lesson);
    }

    /**
     * Обновление урока.
     *
     * @param id  ID урока
     * @param dto Данные для обновления урока
     * @return Обновленный урок
     */
    @Transactional
    public LessonResponseDto updateLesson(Long id, @Valid LessonUpdateDto dto) {
        log.info("Updating lesson with ID: {}", id);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Lesson with id '%s' not found", id)));

        if (dto.getTitle() != null) {
            lesson.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            lesson.setContent(dto.getContent());
        }
        if (dto.getVideoUrl() != null) {
            lesson.setVideoUrl(dto.getVideoUrl());
        }

        lesson = lessonRepository.save(lesson);
        log.info("Lesson updated with ID: {}", lesson.getId());

        return lessonResponseDtoFromLesson(lesson);
    }

    /**
     * Удаление урока.
     *
     * @param id ID урока
     */
    @Transactional
    public void deleteLesson(Long id) {
        log.info("Deleting lesson with ID: {}", id);

        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Lesson with id '%s' not found", id));
        }

        lessonRepository.deleteById(id);
        log.info("Lesson deleted with ID: {}", id);
    }

    /**
     * Получение всех уроков по ID модуля.
     *
     * @param moduleId ID модуля
     * @return Список уроков
     */
    public List<LessonResponseDto> getLessonsByModuleId(Long moduleId) {
        log.info("Getting lessons for module with ID: {}", moduleId);

        List<Lesson> lessons = lessonRepository.findByModuleIdWithModule(moduleId);
        return lessons.stream()
                .map(this::lessonResponseDtoFromLesson)
                .collect(Collectors.toList());
    }

    private LessonResponseDto lessonResponseDtoFromLesson(Lesson lesson) {
        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setModuleId(lesson.getModule().getId());
        dto.setModuleTitle(lesson.getModule().getTitle());
        return dto;
    }
}