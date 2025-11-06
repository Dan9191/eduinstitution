package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.Enrollment;
import ru.dan.eduinstitution.entity.EnrollmentId;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.EnrollmentRequestDto;
import ru.dan.eduinstitution.model.EnrollmentResponseDto;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.EnrollmentRepository;
import ru.dan.eduinstitution.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для управления записями студентов на курсы.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * Записать студента на курс.
     *
     * @param dto Данные для записи
     * @return Результат записи
     */
    @Transactional
    public EnrollmentResponseDto enrollStudent(@Valid EnrollmentRequestDto dto) {
        log.info("Enrolling student with ID {} to course with ID {}", dto.getStudentId(), dto.getCourseId());

        // Проверяем, что студент существует
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with id '%s' not found", dto.getStudentId())));

        // Проверяем, что курс существует
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course with id '%s' not found", dto.getCourseId())));

        // Проверяем, что студент не записан уже на этот курс
        if (enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new IllegalStateException(
                    String.format("Student with id '%s' is already enrolled in course with id '%s'",
                            dto.getStudentId(), dto.getCourseId()));
        }

        // Создаем запись
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setUserId(student.getId());
        enrollment.setCourseId(course.getId());
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus("Active"); // по умолчанию активная запись

        // Сохраняем
        enrollmentRepository.save(enrollment);
        log.info("Student with ID {} successfully enrolled to course with ID {}", dto.getStudentId(), dto.getCourseId());

        return enrollmentResponseDtoFromEnrollment(enrollment);
    }

    /**
     * Отписать студента от курса.
     *
     * @param studentId ID студента
     * @param courseId  ID курса
     */
    @Transactional
    public void unenrollStudent(Long studentId, Long courseId) {
        log.info("Unenrolling student with ID {} from course with ID {}", studentId, courseId);

        EnrollmentId enrollmentId = new EnrollmentId();
        enrollmentId.setUserId(studentId);
        enrollmentId.setCourseId(courseId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Enrollment with student ID '%s' and course ID '%s' not found", 
                                studentId, courseId)));

        enrollmentRepository.delete(enrollment);
        log.info("Student with ID {} successfully unenrolled from course with ID {}", studentId, courseId);
    }

    /**
     * Получить все записи студента.
     *
     * @param studentId ID студента
     * @return Список записей
     */
    @Transactional
    public List<EnrollmentResponseDto> getStudentEnrollments(Long studentId) {
        log.info("Getting enrollments for student with ID {}", studentId);

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(this::enrollmentResponseDtoFromEnrollment)
                .collect(Collectors.toList());
    }

    /**
     * Получить всех студентов, записанных на курс.
     *
     * @param courseId ID курса
     * @return Список записей
     */
    @Transactional
    public List<EnrollmentResponseDto> getCourseEnrollments(Long courseId) {
        log.info("Getting enrollments for course with ID {}", courseId);

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream()
                .map(this::enrollmentResponseDtoFromEnrollment)
                .collect(Collectors.toList());
    }

    /**
     * Обновление статуса записи.
     *
     * @param studentId ID студента
     * @param courseId ID курса
     * @param newStatus Новый статус
     * @return Обновленная запись
     */
    @Transactional
    public EnrollmentResponseDto updateEnrollmentStatus(Long studentId, Long courseId, String newStatus) {
        log.info("Updating enrollment status for student ID {} and course ID {} to {}", studentId, courseId, newStatus);

        EnrollmentId enrollmentId = new EnrollmentId();
        enrollmentId.setUserId(studentId);
        enrollmentId.setCourseId(courseId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Enrollment with student ID '%s' and course ID '%s' not found", 
                                studentId, courseId)));

        enrollment.setStatus(newStatus);
        enrollment = enrollmentRepository.save(enrollment);

        log.info("Enrollment status updated for student ID {} and course ID {} to {}", studentId, courseId, newStatus);

        return enrollmentResponseDtoFromEnrollment(enrollment);
    }

    /**
     * Получить список записей по статусу.
     *
     * @param status Статус записей
     * @return Список записей
     */
    public List<EnrollmentResponseDto> getEnrollmentsByStatus(String status) {
        log.info("Getting enrollments with status: {}", status);

        List<Enrollment> enrollments = enrollmentRepository.findByStatus(status);
        return enrollments.stream()
                .map(this::enrollmentResponseDtoFromEnrollment)
                .collect(Collectors.toList());
    }

    private EnrollmentResponseDto enrollmentResponseDtoFromEnrollment(Enrollment enrollment) {
        EnrollmentResponseDto dto = new EnrollmentResponseDto();
        dto.setStudentId(enrollment.getUserId());
        dto.setStudentName(enrollment.getStudent().getName());
        dto.setCourseId(enrollment.getCourseId());
        dto.setCourseTitle(enrollment.getCourse().getTitle());
        dto.setEnrollDate(enrollment.getEnrollDate());
        dto.setStatus(enrollment.getStatus());
        return dto;
    }
}