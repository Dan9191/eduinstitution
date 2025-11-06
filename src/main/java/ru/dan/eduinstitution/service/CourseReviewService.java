package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.CourseReview;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.CourseReviewCreateDto;
import ru.dan.eduinstitution.model.CourseReviewResponseDto;
import ru.dan.eduinstitution.model.CourseReviewUpdateDto;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.CourseReviewRepository;
import ru.dan.eduinstitution.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с отзывами о курсах.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseReviewService {

    private final CourseReviewRepository courseReviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    /**
     * Добавление отзыва о курсе (addReview method).
     *
     * @param dto Данные для создания отзыва
     * @return Созданный отзыв
     */
    @Transactional
    public CourseReviewResponseDto addReview(@Valid CourseReviewCreateDto dto) {
        log.info("Adding review for course ID: {} by student ID: {} with rating: {}", 
                dto.getCourseId(), dto.getStudentId(), dto.getRating());

        // Проверяем, что курс существует
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course with id '%s' not found", dto.getCourseId())));

        // Проверяем, что студент существует
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with id '%s' not found", dto.getStudentId())));

        // Проверяем, нет ли уже существующего отзыва для этого курса и студента
        courseReviewRepository.findByCourseIdAndStudentId(dto.getCourseId(), dto.getStudentId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            String.format("Review already exists for course ID '%s' and student ID '%s'", 
                                    dto.getCourseId(), dto.getStudentId()));
                });

        CourseReview courseReview = new CourseReview();
        courseReview.setCourse(course);
        courseReview.setStudent(student);
        courseReview.setRating(dto.getRating());
        courseReview.setComment(dto.getComment());
        courseReview.setCreatedAt(LocalDateTime.now());

        courseReview = courseReviewRepository.save(courseReview);
        log.info("Review added with ID: {}", courseReview.getId());

        return courseReviewResponseDtoFromCourseReview(courseReview);
    }

    /**
     * Получение отзыва по ID.
     *
     * @param id ID отзыва
     * @return Отзыв
     */
    public CourseReviewResponseDto getCourseReviewById(Long id) {
        log.info("Getting course review by ID: {}", id);

        CourseReview courseReview = courseReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("CourseReview with id '%s' not found", id)));

        return courseReviewResponseDtoFromCourseReview(courseReview);
    }

    /**
     * Обновление отзыва.
     *
     * @param id  ID отзыва
     * @param dto Данные для обновления отзыва
     * @return Обновленный отзыв
     */
    @Transactional
    public CourseReviewResponseDto updateCourseReview(Long id, @Valid CourseReviewUpdateDto dto) {
        log.info("Updating course review with ID: {}", id);

        CourseReview courseReview = courseReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("CourseReview with id '%s' not found", id)));

        if (dto.getRating() != null) {
            courseReview.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            courseReview.setComment(dto.getComment());
        }

        courseReview = courseReviewRepository.save(courseReview);
        log.info("Course review updated with ID: {}", courseReview.getId());

        return courseReviewResponseDtoFromCourseReview(courseReview);
    }

    /**
     * Удаление отзыва.
     *
     * @param id ID отзыва
     */
    @Transactional
    public void deleteCourseReview(Long id) {
        log.info("Deleting course review with ID: {}", id);

        if (!courseReviewRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("CourseReview with id '%s' not found", id));
        }

        courseReviewRepository.deleteById(id);
        log.info("Course review deleted with ID: {}", id);
    }

    /**
     * Получение всех отзывов по ID курса (getReviewsByCourse method).
     *
     * @param courseId ID курса
     * @return Список отзывов
     */
    public List<CourseReviewResponseDto> getReviewsByCourse(Long courseId) {
        log.info("Getting reviews for course with ID: {}", courseId);

        List<CourseReview> courseReviews = courseReviewRepository.findByCourseId(courseId);
        return courseReviews.stream()
                .map(this::courseReviewResponseDtoFromCourseReview)
                .collect(Collectors.toList());
    }

    /**
     * Получение всех отзывов по ID студента.
     *
     * @param studentId ID студента
     * @return Список отзывов
     */
    public List<CourseReviewResponseDto> getReviewsByStudent(Long studentId) {
        log.info("Getting reviews for student with ID: {}", studentId);

        List<CourseReview> courseReviews = courseReviewRepository.findByStudentId(studentId);
        return courseReviews.stream()
                .map(this::courseReviewResponseDtoFromCourseReview)
                .collect(Collectors.toList());
    }

    /**
     * Получение среднего рейтинга по ID курса.
     *
     * @param courseId ID курса
     * @return Средний рейтинг
     */
    public Double getAverageRatingByCourse(Long courseId) {
        log.info("Getting average rating for course with ID: {}", courseId);

        return courseReviewRepository.findAverageRatingByCourseId(courseId);
    }

    private CourseReviewResponseDto courseReviewResponseDtoFromCourseReview(CourseReview courseReview) {
        CourseReviewResponseDto dto = new CourseReviewResponseDto();
        dto.setId(courseReview.getId());
        dto.setRating(courseReview.getRating());
        dto.setComment(courseReview.getComment());
        dto.setCreatedAt(courseReview.getCreatedAt());
        if (courseReview.getCourse() != null) {
            dto.setCourseId(courseReview.getCourse().getId());
            dto.setCourseTitle(courseReview.getCourse().getTitle());
        }
        if (courseReview.getStudent() != null) {
            dto.setStudentId(courseReview.getStudent().getId());
            dto.setStudentName(courseReview.getStudent().getName());
        }
        return dto;
    }
}