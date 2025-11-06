package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Module;
import ru.dan.eduinstitution.entity.Quiz;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.QuizCreateDto;
import ru.dan.eduinstitution.model.QuizResponseDto;
import ru.dan.eduinstitution.model.QuizUpdateDto;
import ru.dan.eduinstitution.repository.ModuleRepository;
import ru.dan.eduinstitution.repository.QuizRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с тестами.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final ModuleRepository moduleRepository;

    /**
     * Создание теста.
     *
     * @param dto Данные для создания теста
     * @return Созданный тест
     */
    @Transactional
    public QuizResponseDto createQuiz(@Valid QuizCreateDto dto) {
        log.info("Creating quiz with title: {}", dto.getTitle());

        // Проверяем, что модуль существует
        Module module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Module with id '%s' not found", dto.getModuleId())));

        Quiz quiz = new Quiz();
        quiz.setModule(module);
        quiz.setTitle(dto.getTitle());
        quiz.setTimeLimit(dto.getTimeLimit());

        quiz = quizRepository.save(quiz);
        log.info("Quiz created with ID: {}", quiz.getId());

        return quizResponseDtoFromQuiz(quiz);
    }

    /**
     * Получение теста по ID.
     *
     * @param id ID теста
     * @return Тест
     */
    public QuizResponseDto getQuizById(Long id) {
        log.info("Getting quiz by ID: {}", id);

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Quiz with id '%s' not found", id)));

        return quizResponseDtoFromQuiz(quiz);
    }

    /**
     * Обновление теста.
     *
     * @param id  ID теста
     * @param dto Данные для обновления теста
     * @return Обновленный тест
     */
    @Transactional
    public QuizResponseDto updateQuiz(Long id, @Valid QuizUpdateDto dto) {
        log.info("Updating quiz with ID: {}", id);

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Quiz with id '%s' not found", id)));

        if (dto.getTitle() != null) {
            quiz.setTitle(dto.getTitle());
        }
        if (dto.getTimeLimit() != null) {
            quiz.setTimeLimit(dto.getTimeLimit());
        }

        quiz = quizRepository.save(quiz);
        log.info("Quiz updated with ID: {}", quiz.getId());

        return quizResponseDtoFromQuiz(quiz);
    }

    /**
     * Удаление теста.
     *
     * @param id ID теста
     */
    @Transactional
    public void deleteQuiz(Long id) {
        log.info("Deleting quiz with ID: {}", id);

        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Quiz with id '%s' not found", id));
        }

        quizRepository.deleteById(id);
        log.info("Quiz deleted with ID: {}", id);
    }

    /**
     * Получение теста по ID модуля.
     *
     * @param moduleId ID модуля
     * @return Тест или null если не найден
     */
    public QuizResponseDto getQuizByModuleId(Long moduleId) {
        log.info("Getting quiz for module with ID: {}", moduleId);

        return quizRepository.findByModuleId(moduleId)
                .map(this::quizResponseDtoFromQuiz)
                .orElse(null);
    }

    private QuizResponseDto quizResponseDtoFromQuiz(Quiz quiz) {
        QuizResponseDto dto = new QuizResponseDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());
        if (quiz.getModule() != null) {
            dto.setModuleId(quiz.getModule().getId());
            dto.setModuleTitle(quiz.getModule().getTitle());
        }
        return dto;
    }
}