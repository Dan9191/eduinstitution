package ru.dan.eduinstitution.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dan.eduinstitution.model.EnrollmentRequestDto;
import ru.dan.eduinstitution.model.EnrollmentResponseDto;
import ru.dan.eduinstitution.service.EnrollmentService;

import java.util.List;

@Tag(name = "Enrollment", description = "Operations related to student enrollments")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(
            summary = "Enroll a student to a course",
            description = "Enrolls a student to a course with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Student enrolled successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnrollmentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Student or course not found"),
                    @ApiResponse(responseCode = "409", description = "Student already enrolled in the course")
            }
    )
    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentResponseDto> enrollStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Enrollment details",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentRequestDto.class))
            ) @Valid @RequestBody EnrollmentRequestDto dto) {
        log.info("Enrolling student to course with request: studentId={}, courseId={}", 
                dto.getStudentId(), dto.getCourseId());
        
        EnrollmentResponseDto responseDto = enrollmentService.enrollStudent(dto);
        log.info("Student enrolled with ID: {}", responseDto.getStudentId());
        
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Unenroll a student from a course",
            description = "Removes the enrollment of a student from a course",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Student unenrolled successfully"),
                    @ApiResponse(responseCode = "404", description = "Enrollment not found")
            }
    )
    @DeleteMapping("/unenroll/{studentId}/{courseId}")
    public ResponseEntity<Void> unenrollStudent(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID of the student to unenroll")
            @PathVariable Long studentId,
            @io.swagger.v3.oas.annotations.Parameter(description = "ID of the course to unenroll from")
            @PathVariable Long courseId) {
        log.info("Unenrolling student with ID {} from course with ID {}", studentId, courseId);
        
        enrollmentService.unenrollStudent(studentId, courseId);
        log.info("Student with ID {} unenrolled from course with ID {}", studentId, courseId);
        
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all enrollments for a student",
            description = "Retrieves all course enrollments for a specific student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of student enrollments retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnrollmentResponseDto[].class)))
            }
    )
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDto>> getStudentEnrollments(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID of the student")
            @PathVariable Long studentId) {
        log.info("Getting enrollments for student with ID {}", studentId);
        
        List<EnrollmentResponseDto> enrollments = enrollmentService.getStudentEnrollments(studentId);
        log.info("Retrieved {} enrollments for student with ID {}", enrollments.size(), studentId);
        
        return ResponseEntity.ok(enrollments);
    }

    @Operation(
            summary = "Get all students enrolled in a course",
            description = "Retrieves all student enrollments for a specific course",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of course enrollments retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnrollmentResponseDto[].class)))
            }
    )
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponseDto>> getCourseEnrollments(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID of the course")
            @PathVariable Long courseId) {
        log.info("Getting enrollments for course with ID {}", courseId);
        
        List<EnrollmentResponseDto> enrollments = enrollmentService.getCourseEnrollments(courseId);
        log.info("Retrieved {} student enrollments for course with ID {}", enrollments.size(), courseId);
        
        return ResponseEntity.ok(enrollments);
    }
}