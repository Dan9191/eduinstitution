package ru.dan.eduinstitution.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dan.eduinstitution.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByCategoryId(Long categoryId);

    List<Course> findByTitleContainingIgnoreCase(String keyword);

    @Query("SELECT c FROM Course c JOIN c.tags t WHERE t.name = :tagName")
    List<Course> findByTagName(String tagName);

    Page<Course> findAll(Pageable pageable);
    
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.teacher LEFT JOIN FETCH c.category")
    Page<Course> findAllWithTeacherAndCategory(Pageable pageable);
}
