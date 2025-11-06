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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dan.eduinstitution.model.SubmissionCreateDto;
import ru.dan.eduinstitution.model.SubmissionGradeDto;
import ru.dan.eduinstitution.model.SubmissionResponseDto;
import ru.dan.eduinstitution.service.SubmissionService;

import java.util.List;

@Tag(name = "Submission", description = "Operations related to submissions")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/submission")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(
            summary = "Create a new submission",
            description = "Creates a new submission with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Submission created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubmissionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Assignment or student not found"),
                    @ApiResponse(responseCode = "409", description = "Submission already exists for this assignment and student")
            }
    )
    @PostMapping
    public ResponseEntity<SubmissionResponseDto> createSubmission(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Submission details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubmissionCreateDto.class))
            ) @Valid @RequestBody SubmissionCreateDto dto) {
        log.info("Creating submission for assignment ID: {} by student ID: {}", 
                dto.getAssignmentId(), dto.getStudentId());
        SubmissionResponseDto responseDto = submissionService.createSubmission(dto);
        log.info("Submission created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get submission by ID",
            description = "Retrieves a submission by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Submission retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubmissionResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Submission not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponseDto> getSubmissionById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the submission")
            @PathVariable Long id) {
        log.info("Getting submission by ID: {}", id);
        SubmissionResponseDto responseDto = submissionService.getSubmissionById(id);
        log.info("Submission retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Grade a submission",
            description = "Grades a submission with the provided score and feedback",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Submission graded successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubmissionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Submission not found")
            }
    )
    @PutMapping("/{id}/grade")
    public ResponseEntity<SubmissionResponseDto> gradeSubmission(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the submission")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Grade details for the submission",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubmissionGradeDto.class))
            ) @Valid @RequestBody SubmissionGradeDto gradeDto) {
        log.info("Grading submission with ID: {} with score: {}", id, gradeDto.getScore());
        SubmissionResponseDto responseDto = submissionService.gradeSubmission(id, gradeDto);
        log.info("Submission graded with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Get all submissions for a student",
            description = "Retrieves all submissions for a specific student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of student submissions retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubmissionResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByStudentId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the student")
            @PathVariable Long studentId) {
        log.info("Getting submissions for student with ID: {}", studentId);
        List<SubmissionResponseDto> responseDtos = submissionService.getSubmissionsByStudentId(studentId);
        log.info("Retrieved {} submissions for student with ID: {}", responseDtos.size(), studentId);
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "Get all submissions for an assignment",
            description = "Retrieves all submissions for a specific assignment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of assignment submissions retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubmissionResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Assignment not found")
            }
    )
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByAssignmentId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the assignment")
            @PathVariable Long assignmentId) {
        log.info("Getting submissions for assignment with ID: {}", assignmentId);
        List<SubmissionResponseDto> responseDtos = submissionService.getSubmissionsByAssignmentId(assignmentId);
        log.info("Retrieved {} submissions for assignment with ID: {}", responseDtos.size(), assignmentId);
        return ResponseEntity.ok(responseDtos);
    }
}