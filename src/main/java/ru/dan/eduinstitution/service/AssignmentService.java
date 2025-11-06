package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Assignment;
import ru.dan.eduinstitution.entity.Lesson;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.AssignmentCreateDto;
import ru.dan.eduinstitution.model.AssignmentResponseDto;
import ru.dan.eduinstitution.model.AssignmentUpdateDto;
import ru.dan.eduinstitution.repository.AssignmentRepository;
import ru.dan.eduinstitution.repository.LessonRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с заданиями.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;

    /**
     * Создание задания.
     *
     * @param dto Данные для создания задания
     * @return Созданное задание
     */
    @Transactional
    public AssignmentResponseDto createAssignment(@Valid AssignmentCreateDto dto) {
        log.info("Creating assignment with title: {}", dto.getTitle());

        // Проверяем, что урок существует
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Lesson with id '%s' not found", dto.getLessonId())));

        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setDueDate(dto.getDueDate());
        assignment.setMaxScore(dto.getMaxScore());

        assignment = assignmentRepository.save(assignment);
        log.info("Assignment created with ID: {}", assignment.getId());

        return assignmentResponseDtoFromAssignment(assignment);
    }

    /**
     * Получение задания по ID.
     *
     * @param id ID задания
     * @return Задание
     */
    public AssignmentResponseDto getAssignmentById(Long id) {
        log.info("Getting assignment by ID: {}", id);

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Assignment with id '%s' not found", id)));

        return assignmentResponseDtoFromAssignment(assignment);
    }

    /**
     * Обновление задания.
     *
     * @param id  ID задания
     * @param dto Данные для обновления задания
     * @return Обновленное задание
     */
    @Transactional
    public AssignmentResponseDto updateAssignment(Long id, @Valid AssignmentUpdateDto dto) {
        log.info("Updating assignment with ID: {}", id);

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Assignment with id '%s' not found", id)));

        if (dto.getTitle() != null) {
            assignment.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            assignment.setDescription(dto.getDescription());
        }
        if (dto.getDueDate() != null) {
            assignment.setDueDate(dto.getDueDate());
        }
        if (dto.getMaxScore() != null) {
            assignment.setMaxScore(dto.getMaxScore());
        }

        assignment = assignmentRepository.save(assignment);
        log.info("Assignment updated with ID: {}", assignment.getId());

        return assignmentResponseDtoFromAssignment(assignment);
    }

    /**
     * Удаление задания.
     *
     * @param id ID задания
     */
    @Transactional
    public void deleteAssignment(Long id) {
        log.info("Deleting assignment with ID: {}", id);

        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Assignment with id '%s' not found", id));
        }

        assignmentRepository.deleteById(id);
        log.info("Assignment deleted with ID: {}", id);
    }

    /**
     * Получение всех заданий по ID урока.
     *
     * @param lessonId ID урока
     * @return Список заданий
     */
    public List<AssignmentResponseDto> getAssignmentsByLessonId(Long lessonId) {
        log.info("Getting assignments for lesson with ID: {}", lessonId);

        List<Assignment> assignments = assignmentRepository.findByLessonId(lessonId);
        return assignments.stream()
                .map(this::assignmentResponseDtoFromAssignment)
                .collect(Collectors.toList());
    }

    private AssignmentResponseDto assignmentResponseDtoFromAssignment(Assignment assignment) {
        AssignmentResponseDto dto = new AssignmentResponseDto();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxScore(assignment.getMaxScore());
        dto.setLessonId(assignment.getLesson().getId());
        dto.setLessonTitle(assignment.getLesson().getTitle());
        return dto;
    }
}