package ru.dan.eduinstitution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dan.eduinstitution.entity.CourseReview;

import java.util.List;
import java.util.Optional;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    List<CourseReview> findByCourseId(Long courseId);

    List<CourseReview> findByStudentId(Long studentId);

    Optional<CourseReview> findByCourseIdAndStudentId(Long courseId, Long studentId);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(cr.rating) FROM CourseReview cr WHERE cr.course.id = :courseId")
    Double findAverageRatingByCourseId(Long courseId);
}