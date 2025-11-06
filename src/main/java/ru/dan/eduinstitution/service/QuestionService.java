package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Question;
import ru.dan.eduinstitution.entity.Quiz;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.QuestionCreateDto;
import ru.dan.eduinstitution.model.QuestionResponseDto;
import ru.dan.eduinstitution.model.QuestionUpdateDto;
import ru.dan.eduinstitution.repository.QuestionRepository;
import ru.dan.eduinstitution.repository.QuizRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с вопросами.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    /**
     * Создание вопроса.
     *
     * @param dto Данные для создания вопроса
     * @return Созданный вопрос
     */
    @Transactional
    public QuestionResponseDto createQuestion(@Valid QuestionCreateDto dto) {
        log.info("Creating question with text: {}", dto.getText());

        // Проверяем, что тест существует
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Quiz with id '%s' not found", dto.getQuizId())));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(dto.getText());
        question.setType(Question.QuestionType.valueOf(dto.getType()));

        question = questionRepository.save(question);
        log.info("Question created with ID: {}", question.getId());

        return questionResponseDtoFromQuestion(question);
    }

    /**
     * Получение вопроса по ID.
     *
     * @param id ID вопроса
     * @return Вопрос
     */
    public QuestionResponseDto getQuestionById(Long id) {
        log.info("Getting question by ID: {}", id);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Question with id '%s' not found", id)));

        return questionResponseDtoFromQuestion(question);
    }

    /**
     * Обновление вопроса.
     *
     * @param id  ID вопроса
     * @param dto Данные для обновления вопроса
     * @return Обновленный вопрос
     */
    @Transactional
    public QuestionResponseDto updateQuestion(Long id, @Valid QuestionUpdateDto dto) {
        log.info("Updating question with ID: {}", id);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Question with id '%s' not found", id)));

        if (dto.getText() != null) {
            question.setText(dto.getText());
        }
        if (dto.getType() != null) {
            question.setType(Question.QuestionType.valueOf(dto.getType()));
        }

        question = questionRepository.save(question);
        log.info("Question updated with ID: {}", question.getId());

        return questionResponseDtoFromQuestion(question);
    }

    /**
     * Удаление вопроса.
     *
     * @param id ID вопроса
     */
    @Transactional
    public void deleteQuestion(Long id) {
        log.info("Deleting question with ID: {}", id);

        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Question with id '%s' not found", id));
        }

        questionRepository.deleteById(id);
        log.info("Question deleted with ID: {}", id);
    }

    /**
     * Получение всех вопросов по ID теста.
     *
     * @param quizId ID теста
     * @return Список вопросов
     */
    public List<QuestionResponseDto> getQuestionsByQuizId(Long quizId) {
        log.info("Getting questions for quiz with ID: {}", quizId);

        List<Question> questions = questionRepository.findByQuizIdOrderByIdAsc(quizId);
        return questions.stream()
                .map(this::questionResponseDtoFromQuestion)
                .collect(Collectors.toList());
    }

    private QuestionResponseDto questionResponseDtoFromQuestion(Question question) {
        QuestionResponseDto dto = new QuestionResponseDto();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setType(question.getType().name());
        if (question.getQuiz() != null) {
            dto.setQuizId(question.getQuiz().getId());
            dto.setQuizTitle(question.getQuiz().getTitle());
        }
        return dto;
    }
}