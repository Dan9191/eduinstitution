package ru.dan.eduinstitution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dan.eduinstitution.entity.Module;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByCourseId(Long courseId);

    @Query("SELECT m FROM Module m JOIN FETCH m.course WHERE m.course.id = :courseId ORDER BY m.orderIndex")
    List<Module> findByCourseIdWithCourse(Long courseId);
}