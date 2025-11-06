package ru.dan.eduinstitution.service;

import org.springframework.stereotype.Component;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.Enrollment;
import ru.dan.eduinstitution.entity.Submission;
import ru.dan.eduinstitution.entity.QuizSubmission;
import ru.dan.eduinstitution.entity.CourseReview;
import ru.dan.eduinstitution.model.CourseResponseDto;
import ru.dan.eduinstitution.model.EnrollmentResponseDto;
import ru.dan.eduinstitution.model.SubmissionResponseDto;
import ru.dan.eduinstitution.model.QuizSubmissionResponseDto;
import ru.dan.eduinstitution.model.CourseReviewResponseDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Компонент для маппинга сущностей в DTO.
 */
@Component
public class EntityToDtoMapper {

    public CourseResponseDto mapCourseToDto(Course course) {
        if (course == null) {
            return null;
        }

        CourseResponseDto dto = new CourseResponseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        if (course.getCategory() != null) {
            dto.setCategoryId(course.getCategory().getId());
            dto.setCategoryName(course.getCategory().getName());
        }
        if (course.getTeacher() != null) {
            dto.setTeacherId(course.getTeacher().getId());
            dto.setTeacherName(course.getTeacher().getName());
        }
        dto.setDuration(course.getDuration());
        dto.setStartDate(course.getStartDate());

        return dto;
    }

    public EnrollmentResponseDto mapEnrollmentToDto(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        EnrollmentResponseDto dto = new EnrollmentResponseDto();
        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getId());
            dto.setStudentName(enrollment.getStudent().getName());
        }
        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getId());
            dto.setCourseTitle(enrollment.getCourse().getTitle());
        }
        dto.setEnrollDate(enrollment.getEnrollDate());
        dto.setStatus(enrollment.getStatus());

        return dto;
    }

    public SubmissionResponseDto mapSubmissionToDto(Submission submission) {
        if (submission == null) {
            return null;
        }

        SubmissionResponseDto dto = new SubmissionResponseDto();
        dto.setId(submission.getId());
        if (submission.getAssignment() != null) {
            dto.setAssignmentId(submission.getAssignment().getId());
            dto.setAssignmentTitle(submission.getAssignment().getTitle());
        }
        if (submission.getStudent() != null) {
            dto.setStudentId(submission.getStudent().getId());
            dto.setStudentName(submission.getStudent().getName());
        }
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setContent(submission.getContent());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());

        return dto;
    }

    public QuizSubmissionResponseDto mapQuizSubmissionToDto(QuizSubmission quizSubmission) {
        if (quizSubmission == null) {
            return null;
        }

        QuizSubmissionResponseDto dto = new QuizSubmissionResponseDto();
        dto.setId(quizSubmission.getId());
        if (quizSubmission.getQuiz() != null) {
            dto.setQuizId(quizSubmission.getQuiz().getId());
            dto.setQuizTitle(quizSubmission.getQuiz().getTitle());
        }
        if (quizSubmission.getStudent() != null) {
            dto.setStudentId(quizSubmission.getStudent().getId());
            dto.setStudentName(quizSubmission.getStudent().getName());
        }
        dto.setScore(quizSubmission.getScore());
        dto.setTakenAt(quizSubmission.getTakenAt());

        return dto;
    }

    public CourseReviewResponseDto mapCourseReviewToDto(CourseReview courseReview) {
        if (courseReview == null) {
            return null;
        }

        CourseReviewResponseDto dto = new CourseReviewResponseDto();
        dto.setId(courseReview.getId());
        if (courseReview.getCourse() != null) {
            dto.setCourseId(courseReview.getCourse().getId());
            dto.setCourseTitle(courseReview.getCourse().getTitle());
        }
        if (courseReview.getStudent() != null) {
            dto.setStudentId(courseReview.getStudent().getId());
            dto.setStudentName(courseReview.getStudent().getName());
        }
        dto.setRating(courseReview.getRating());
        dto.setComment(courseReview.getComment());
        dto.setCreatedAt(courseReview.getCreatedAt());

        return dto;
    }

    public List<CourseResponseDto> mapCoursesToDtos(List<Course> courses) {
        return courses.stream()
                .map(this::mapCourseToDto)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponseDto> mapEnrollmentsToDtos(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(this::mapEnrollmentToDto)
                .collect(Collectors.toList());
    }

    public List<SubmissionResponseDto> mapSubmissionsToDtos(List<Submission> submissions) {
        return submissions.stream()
                .map(this::mapSubmissionToDto)
                .collect(Collectors.toList());
    }

    public List<QuizSubmissionResponseDto> mapQuizSubmissionsToDtos(List<QuizSubmission> quizSubmissions) {
        return quizSubmissions.stream()
                .map(this::mapQuizSubmissionToDto)
                .collect(Collectors.toList());
    }

    public List<CourseReviewResponseDto> mapCourseReviewsToDtos(List<CourseReview> courseReviews) {
        return courseReviews.stream()
                .map(this::mapCourseReviewToDto)
                .collect(Collectors.toList());
    }
}