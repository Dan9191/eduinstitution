package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Quiz;
import ru.dan.eduinstitution.entity.QuizSubmission;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.QuizSubmissionCreateDto;
import ru.dan.eduinstitution.model.QuizSubmissionResponseDto;
import ru.dan.eduinstitution.model.QuizSubmissionUpdateDto;
import ru.dan.eduinstitution.repository.QuizRepository;
import ru.dan.eduinstitution.repository.QuizSubmissionRepository;
import ru.dan.eduinstitution.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с результатами тестов.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuizSubmissionService {

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    /**
     * Создание результата теста.
     *
     * @param dto Данные для создания результата теста
     * @return Созданный результат теста
     */
    @Transactional
    public QuizSubmissionResponseDto createQuizSubmission(@Valid QuizSubmissionCreateDto dto) {
        log.info("Creating quiz submission for quiz ID: {} by student ID: {}", 
                dto.getQuizId(), dto.getStudentId());

        // Проверяем, что тест существует
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Quiz with id '%s' not found", dto.getQuizId())));

        // Проверяем, что студент существует
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with id '%s' not found", dto.getStudentId())));

        // Проверяем, нет ли уже существующего submission для этого теста и студента
        quizSubmissionRepository.findByQuizIdAndStudentId(dto.getQuizId(), dto.getStudentId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            String.format("Quiz submission already exists for quiz ID '%s' and student ID '%s'", 
                                    dto.getQuizId(), dto.getStudentId()));
                });

        QuizSubmission quizSubmission = new QuizSubmission();
        quizSubmission.setQuiz(quiz);
        quizSubmission.setStudent(student);
        quizSubmission.setScore(dto.getScore());
        quizSubmission.setTakenAt(LocalDateTime.now());

        quizSubmission = quizSubmissionRepository.save(quizSubmission);
        log.info("Quiz submission created with ID: {}", quizSubmission.getId());

        return quizSubmissionResponseDtoFromQuizSubmission(quizSubmission);
    }

    /**
     * Получение результата теста по ID.
     *
     * @param id ID результата теста
     * @return Результат теста
     */
    public QuizSubmissionResponseDto getQuizSubmissionById(Long id) {
        log.info("Getting quiz submission by ID: {}", id);

        QuizSubmission quizSubmission = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("QuizSubmission with id '%s' not found", id)));

        return quizSubmissionResponseDtoFromQuizSubmission(quizSubmission);
    }

    /**
     * Обновление результата теста.
     *
     * @param id  ID результата теста
     * @param dto Данные для обновления результата теста
     * @return Обновленный результат теста
     */
    @Transactional
    public QuizSubmissionResponseDto updateQuizSubmission(Long id, @Valid QuizSubmissionUpdateDto dto) {
        log.info("Updating quiz submission with ID: {}", id);

        QuizSubmission quizSubmission = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("QuizSubmission with id '%s' not found", id)));

        if (dto.getScore() != null) {
            quizSubmission.setScore(dto.getScore());
        }

        quizSubmission = quizSubmissionRepository.save(quizSubmission);
        log.info("Quiz submission updated with ID: {}", quizSubmission.getId());

        return quizSubmissionResponseDtoFromQuizSubmission(quizSubmission);
    }

    /**
     * Удаление результата теста.
     *
     * @param id ID результата теста
     */
    @Transactional
    public void deleteQuizSubmission(Long id) {
        log.info("Deleting quiz submission with ID: {}", id);

        if (!quizSubmissionRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("QuizSubmission with id '%s' not found", id));
        }

        quizSubmissionRepository.deleteById(id);
        log.info("Quiz submission deleted with ID: {}", id);
    }

    /**
     * Получение всех результатов тестов по ID студента.
     *
     * @param studentId ID студента
     * @return Список результатов тестов
     */
    public List<QuizSubmissionResponseDto> getQuizSubmissionsByStudentId(Long studentId) {
        log.info("Getting quiz submissions for student with ID: {}", studentId);

        List<QuizSubmission> quizSubmissions = quizSubmissionRepository.findByStudentId(studentId);
        return quizSubmissions.stream()
                .map(this::quizSubmissionResponseDtoFromQuizSubmission)
                .collect(Collectors.toList());
    }

    /**
     * Получение всех результатов тестов по ID теста.
     *
     * @param quizId ID теста
     * @return Список результатов тестов
     */
    public List<QuizSubmissionResponseDto> getQuizSubmissionsByQuizId(Long quizId) {
        log.info("Getting quiz submissions for quiz with ID: {}", quizId);

        List<QuizSubmission> quizSubmissions = quizSubmissionRepository.findByQuizId(quizId);
        return quizSubmissions.stream()
                .map(this::quizSubmissionResponseDtoFromQuizSubmission)
                .collect(Collectors.toList());
    }

    private QuizSubmissionResponseDto quizSubmissionResponseDtoFromQuizSubmission(QuizSubmission quizSubmission) {
        QuizSubmissionResponseDto dto = new QuizSubmissionResponseDto();
        dto.setId(quizSubmission.getId());
        dto.setScore(quizSubmission.getScore());
        dto.setTakenAt(quizSubmission.getTakenAt());
        if (quizSubmission.getQuiz() != null) {
            dto.setQuizId(quizSubmission.getQuiz().getId());
            dto.setQuizTitle(quizSubmission.getQuiz().getTitle());
        }
        if (quizSubmission.getStudent() != null) {
            dto.setStudentId(quizSubmission.getStudent().getId());
            dto.setStudentName(quizSubmission.getStudent().getName());
        }
        return dto;
    }
}