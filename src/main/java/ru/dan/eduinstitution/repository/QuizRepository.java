package ru.dan.eduinstitution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dan.eduinstitution.entity.Quiz;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByModuleId(Long moduleId);
}
