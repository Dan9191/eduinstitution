package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Assignment;
import ru.dan.eduinstitution.entity.Submission;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.SubmissionCreateDto;
import ru.dan.eduinstitution.model.SubmissionGradeDto;
import ru.dan.eduinstitution.model.SubmissionResponseDto;
import ru.dan.eduinstitution.repository.AssignmentRepository;
import ru.dan.eduinstitution.repository.SubmissionRepository;
import ru.dan.eduinstitution.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с ответами/решениями студентов.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    /**
     * Создание ответа/решения студента.
     *
     * @param dto Данные для создания ответа/решения
     * @return Созданный ответ/решение
     */
    @Transactional
    public SubmissionResponseDto createSubmission(@Valid SubmissionCreateDto dto) {
        log.info("Creating submission for assignment ID: {} by student ID: {}", 
                dto.getAssignmentId(), dto.getStudentId());

        // Проверяем, что задание существует
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Assignment with id '%s' not found", dto.getAssignmentId())));

        // Проверяем, что студент существует
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with id '%s' not found", dto.getStudentId())));

        // Проверяем, нет ли уже существующего submission для этого задания и студента
        submissionRepository.findByAssignmentIdAndStudentId(dto.getAssignmentId(), dto.getStudentId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            String.format("Submission already exists for assignment ID '%s' and student ID '%s'", 
                                    dto.getAssignmentId(), dto.getStudentId()));
                });

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setContent(dto.getContent());
        submission.setScore(null); // Оценка не выставлена при создании
        submission.setFeedback(null); // Обратная связь не предоставлена при создании

        submission = submissionRepository.save(submission);
        log.info("Submission created with ID: {}", submission.getId());

        return submissionResponseDtoFromSubmission(submission);
    }

    /**
     * Получение ответа/решения по ID.
     *
     * @param id ID ответа/решения
     * @return Ответ/решение
     */
    public SubmissionResponseDto getSubmissionById(Long id) {
        log.info("Getting submission by ID: {}", id);

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Submission with id '%s' not found", id)));

        return submissionResponseDtoFromSubmission(submission);
    }

    /**
     * Выставление оценки за ответ/решение.
     *
     * @param id ID ответа/решения
     * @param gradeDto Данные для оценки
     * @return Обновленное ответ/решение
     */
    @Transactional
    public SubmissionResponseDto gradeSubmission(Long id, @Valid SubmissionGradeDto gradeDto) {
        log.info("Grading submission with ID: {} with score: {}", id, gradeDto.getScore());

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Submission with id '%s' not found", id)));

        submission.setScore(gradeDto.getScore());
        submission.setFeedback(gradeDto.getFeedback());

        submission = submissionRepository.save(submission);
        log.info("Submission graded with ID: {}", submission.getId());

        return submissionResponseDtoFromSubmission(submission);
    }

    /**
     * Получение всех ответов/решений по ID студента.
     *
     * @param studentId ID студента
     * @return Список ответов/решений
     */
    public List<SubmissionResponseDto> getSubmissionsByStudentId(Long studentId) {
        log.info("Getting submissions for student with ID: {}", studentId);

        List<Submission> submissions = submissionRepository.findByStudentId(studentId);
        return submissions.stream()
                .map(this::submissionResponseDtoFromSubmission)
                .collect(Collectors.toList());
    }

    /**
     * Получение всех ответов/решений по ID задания.
     *
     * @param assignmentId ID задания
     * @return Список ответов/решений
     */
    public List<SubmissionResponseDto> getSubmissionsByAssignmentId(Long assignmentId) {
        log.info("Getting submissions for assignment with ID: {}", assignmentId);

        List<Submission> submissions = submissionRepository.findByAssignmentId(assignmentId);
        return submissions.stream()
                .map(this::submissionResponseDtoFromSubmission)
                .collect(Collectors.toList());
    }

    private SubmissionResponseDto submissionResponseDtoFromSubmission(Submission submission) {
        SubmissionResponseDto dto = new SubmissionResponseDto();
        dto.setId(submission.getId());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setContent(submission.getContent());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setAssignmentTitle(submission.getAssignment().getTitle());
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getName());
        return dto;
    }
}