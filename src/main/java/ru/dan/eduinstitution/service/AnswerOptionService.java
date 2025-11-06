package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.AnswerOption;
import ru.dan.eduinstitution.entity.Question;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.AnswerOptionCreateDto;
import ru.dan.eduinstitution.model.AnswerOptionResponseDto;
import ru.dan.eduinstitution.model.AnswerOptionUpdateDto;
import ru.dan.eduinstitution.repository.AnswerOptionRepository;
import ru.dan.eduinstitution.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с вариантами ответов.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    /**
     * Создание варианта ответа.
     *
     * @param dto Данные для создания варианта ответа
     * @return Созданный вариант ответа
     */
    @Transactional
    public AnswerOptionResponseDto createAnswerOption(@Valid AnswerOptionCreateDto dto) {
        log.info("Creating answer option with text: {}", dto.getText());

        // Проверяем, что вопрос существует
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Question with id '%s' not found", dto.getQuestionId())));

        AnswerOption answerOption = new AnswerOption();
        answerOption.setQuestion(question);
        answerOption.setText(dto.getText());
        answerOption.setCorrect(dto.getIsCorrect() != null && Boolean.TRUE.equals(dto.getIsCorrect()));

        answerOption = answerOptionRepository.save(answerOption);
        log.info("Answer option created with ID: {}", answerOption.getId());

        return answerOptionResponseDtoFromAnswerOption(answerOption);
    }

    /**
     * Получение варианта ответа по ID.
     *
     * @param id ID варианта ответа
     * @return Вариант ответа
     */
    public AnswerOptionResponseDto getAnswerOptionById(Long id) {
        log.info("Getting answer option by ID: {}", id);

        AnswerOption answerOption = answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("AnswerOption with id '%s' not found", id)));

        return answerOptionResponseDtoFromAnswerOption(answerOption);
    }

    /**
     * Обновление варианта ответа.
     *
     * @param id  ID варианта ответа
     * @param dto Данные для обновления варианта ответа
     * @return Обновленный вариант ответа
     */
    @Transactional
    public AnswerOptionResponseDto updateAnswerOption(Long id, @Valid AnswerOptionUpdateDto dto) {
        log.info("Updating answer option with ID: {}", id);

        AnswerOption answerOption = answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("AnswerOption with id '%s' not found", id)));

        if (dto.getText() != null) {
            answerOption.setText(dto.getText());
        }
        if (dto.getIsCorrect() != null) {
            answerOption.setCorrect(Boolean.TRUE.equals(dto.getIsCorrect()));
        }

        answerOption = answerOptionRepository.save(answerOption);
        log.info("Answer option updated with ID: {}", answerOption.getId());

        return answerOptionResponseDtoFromAnswerOption(answerOption);
    }

    /**
     * Удаление варианта ответа.
     *
     * @param id ID варианта ответа
     */
    @Transactional
    public void deleteAnswerOption(Long id) {
        log.info("Deleting answer option with ID: {}", id);

        if (!answerOptionRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("AnswerOption with id '%s' not found", id));
        }

        answerOptionRepository.deleteById(id);
        log.info("Answer option deleted with ID: {}", id);
    }

    /**
     * Получение всех вариантов ответов по ID вопроса.
     *
     * @param questionId ID вопроса
     * @return Список вариантов ответов
     */
    public List<AnswerOptionResponseDto> getAnswerOptionsByQuestionId(Long questionId) {
        log.info("Getting answer options for question with ID: {}", questionId);

        List<AnswerOption> answerOptions = answerOptionRepository.findByQuestionId(questionId);
        return answerOptions.stream()
                .map(this::answerOptionResponseDtoFromAnswerOption)
                .collect(Collectors.toList());
    }

    private AnswerOptionResponseDto answerOptionResponseDtoFromAnswerOption(AnswerOption answerOption) {
        AnswerOptionResponseDto dto = new AnswerOptionResponseDto();
        dto.setId(answerOption.getId());
        dto.setText(answerOption.getText());
        dto.setIsCorrect(answerOption.isCorrect());
        if (answerOption.getQuestion() != null) {
            dto.setQuestionId(answerOption.getQuestion().getId());
            dto.setQuestionText(answerOption.getQuestion().getText());
        }
        return dto;
    }
}