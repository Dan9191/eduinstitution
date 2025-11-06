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
import org.springframework.web.bind.annotation.RestController;
import ru.dan.eduinstitution.model.QuizSubmissionCreateDto;
import ru.dan.eduinstitution.model.QuizSubmissionResponseDto;
import ru.dan.eduinstitution.model.QuizSubmissionUpdateDto;
import ru.dan.eduinstitution.service.QuizSubmissionService;

import java.util.List;

@Tag(name = "QuizSubmission", description = "Operations related to quiz submissions")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz-submission")
public class QuizSubmissionController {

    private final QuizSubmissionService quizSubmissionService;

    @Operation(
            summary = "Create a new quiz submission",
            description = "Creates a new quiz submission with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quiz submission created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizSubmissionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Quiz or student not found"),
                    @ApiResponse(responseCode = "409", description = "Quiz submission already exists for this quiz and student")
            }
    )
    @PostMapping
    public ResponseEntity<QuizSubmissionResponseDto> createQuizSubmission(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Quiz submission details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizSubmissionCreateDto.class))
            ) @Valid @RequestBody QuizSubmissionCreateDto dto) {
        log.info("Creating quiz submission for quiz ID: {} by student ID: {}", 
                dto.getQuizId(), dto.getStudentId());
        QuizSubmissionResponseDto responseDto = quizSubmissionService.createQuizSubmission(dto);
        log.info("Quiz submission created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get quiz submission by ID",
            description = "Retrieves a quiz submission by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz submission retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizSubmissionResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Quiz submission not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<QuizSubmissionResponseDto> getQuizSubmissionById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz submission")
            @PathVariable Long id) {
        log.info("Getting quiz submission by ID: {}", id);
        QuizSubmissionResponseDto responseDto = quizSubmissionService.getQuizSubmissionById(id);
        log.info("Quiz submission retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update quiz submission",
            description = "Updates a quiz submission with the provided score",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz submission updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizSubmissionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Quiz submission not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<QuizSubmissionResponseDto> updateQuizSubmission(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz submission")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Quiz submission details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizSubmissionUpdateDto.class))
            ) @Valid @RequestBody QuizSubmissionUpdateDto dto) {
        log.info("Updating quiz submission with ID: {}", id);
        QuizSubmissionResponseDto responseDto = quizSubmissionService.updateQuizSubmission(id, dto);
        log.info("Quiz submission updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete quiz submission",
            description = "Deletes a quiz submission by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Quiz submission deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Quiz submission not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizSubmission(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz submission")
            @PathVariable Long id) {
        log.info("Deleting quiz submission with ID: {}", id);
        quizSubmissionService.deleteQuizSubmission(id);
        log.info("Quiz submission deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all quiz submissions for a student",
            description = "Retrieves all quiz submissions for a specific student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of student quiz submissions retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizSubmissionResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<QuizSubmissionResponseDto>> getQuizSubmissionsByStudentId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the student")
            @PathVariable Long studentId) {
        log.info("Getting quiz submissions for student with ID: {}", studentId);
        List<QuizSubmissionResponseDto> responseDtos = quizSubmissionService.getQuizSubmissionsByStudentId(studentId);
        log.info("Retrieved {} quiz submissions for student with ID: {}", responseDtos.size(), studentId);
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "Get all quiz submissions for a quiz",
            description = "Retrieves all quiz submissions for a specific quiz",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of quiz submissions retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizSubmissionResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Quiz not found")
            }
    )
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizSubmissionResponseDto>> getQuizSubmissionsByQuizId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz")
            @PathVariable Long quizId) {
        log.info("Getting quiz submissions for quiz with ID: {}", quizId);
        List<QuizSubmissionResponseDto> responseDtos = quizSubmissionService.getQuizSubmissionsByQuizId(quizId);
        log.info("Retrieved {} quiz submissions for quiz with ID: {}", responseDtos.size(), quizId);
        return ResponseEntity.ok(responseDtos);
    }
}