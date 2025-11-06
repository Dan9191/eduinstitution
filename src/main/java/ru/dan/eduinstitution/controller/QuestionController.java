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
import ru.dan.eduinstitution.model.QuestionCreateDto;
import ru.dan.eduinstitution.model.QuestionResponseDto;
import ru.dan.eduinstitution.model.QuestionUpdateDto;
import ru.dan.eduinstitution.service.QuestionService;

import java.util.List;

@Tag(name = "Question", description = "Operations related to questions")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(
            summary = "Create a new question",
            description = "Creates a new question with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Question created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Quiz not found")
            }
    )
    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Question details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionCreateDto.class))
            ) @Valid @RequestBody QuestionCreateDto dto) {
        log.info("Creating question with text: {}", dto.getText());
        QuestionResponseDto responseDto = questionService.createQuestion(dto);
        log.info("Question created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get question by ID",
            description = "Retrieves a question by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Question retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Question not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the question")
            @PathVariable Long id) {
        log.info("Getting question by ID: {}", id);
        QuestionResponseDto responseDto = questionService.getQuestionById(id);
        log.info("Question retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update question",
            description = "Updates a question with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Question updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Question not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the question")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Question details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionUpdateDto.class))
            ) @Valid @RequestBody QuestionUpdateDto dto) {
        log.info("Updating question with ID: {}", id);
        QuestionResponseDto responseDto = questionService.updateQuestion(id, dto);
        log.info("Question updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete question",
            description = "Deletes a question by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Question not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the question")
            @PathVariable Long id) {
        log.info("Deleting question with ID: {}", id);
        questionService.deleteQuestion(id);
        log.info("Question deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all questions for a quiz",
            description = "Retrieves all questions for a specific quiz",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of quiz questions retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Quiz not found")
            }
    )
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByQuizId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz")
            @PathVariable Long quizId) {
        log.info("Getting questions for quiz with ID: {}", quizId);
        List<QuestionResponseDto> responseDtos = questionService.getQuestionsByQuizId(quizId);
        log.info("Retrieved {} questions for quiz with ID: {}", responseDtos.size(), quizId);
        return ResponseEntity.ok(responseDtos);
    }
}