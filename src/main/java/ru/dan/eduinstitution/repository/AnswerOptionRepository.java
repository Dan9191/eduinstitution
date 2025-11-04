package ru.dan.eduinstitution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dan.eduinstitution.entity.AnswerOption;

import java.util.List;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
}
