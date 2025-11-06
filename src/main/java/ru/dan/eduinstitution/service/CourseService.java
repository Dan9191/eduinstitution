package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.CourseCreateDto;
import ru.dan.eduinstitution.model.CourseResponseDto;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Сервис работы с курсами.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryCacheService categoryCacheService;

    private static final int DEFAULT_COURSE_DURATION = 30;

    /**
     * Создание курса.
     *
     * @param dto Модель создания курса.
     * @return Результат выполнения
     */
    @Transactional
    public CourseResponseDto createCourse(@Valid CourseCreateDto dto) throws ResourceNotFoundException {
        log.info("Creating course entity with title: {}", dto.getTitle());
        Course course = new Course();
        course.setTitle(dto.getTitle());
        Optional.of(dto.getDescription()).ifPresent(course::setDescription);
        course.setCategory(categoryCacheService.findById(dto.getCategoryId()));
        Optional<User> teacherOpt = userRepository.findById(dto.getTeacherId());
        if (teacherOpt.isPresent()) {
            course.setTeacher(teacherOpt.get());
        } else {
            throw new ResourceNotFoundException(String.format("User with id '%s' not found", dto.getTeacherId()));
        }
        Optional<LocalDate> startDateOpt = Optional.ofNullable(dto.getStartDate());
        if (startDateOpt.isPresent()) {
            course.setStartDate(startDateOpt.get());
        } else {
            course.setStartDate(LocalDate.now());
        }
        Optional<Integer> durationOpt = Optional.of(dto.getDuration());
        if (startDateOpt.isPresent()) {
            course.setDuration(durationOpt.get());
        } else {
            course.setDuration(DEFAULT_COURSE_DURATION);
        }
        courseRepository.save(course);
        log.info("Course entity saved with ID: {}", course.getId());

        return courseResponseDtoFromCourse(course);
    }


    private CourseResponseDto courseResponseDtoFromCourse(Course course) {
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(course.getId());
        responseDto.setTitle(course.getTitle());
        responseDto.setDescription(course.getDescription());
        responseDto.setCategoryId(course.getCategory().getId());
        responseDto.setCategoryName(course.getCategory().getName());
        responseDto.setTeacherId(course.getTeacher().getId());
        responseDto.setTeacherName(course.getTeacher().getName());
        responseDto.setDuration(course.getDuration());
        responseDto.setStartDate(course.getStartDate());
        return responseDto;
    }

    /**
     * Получение всех курсов с пагинацией.
     *
     * @param pageable Параметры пагинации
     * @return Страница с курсами
     */
    public Page<CourseResponseDto> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAllWithTeacherAndCategory(pageable);
        return courses.map(this::courseResponseDtoFromCourse);
    }
}
