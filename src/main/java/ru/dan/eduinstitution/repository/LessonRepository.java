package ru.dan.eduinstitution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dan.eduinstitution.entity.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByModuleId(Long moduleId);

    @Query("SELECT l FROM Lesson l JOIN FETCH l.module WHERE l.module.id = :moduleId")
    List<Lesson> findByModuleIdWithModule(Long moduleId);
}