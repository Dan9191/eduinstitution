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
import ru.dan.eduinstitution.model.QuizCreateDto;
import ru.dan.eduinstitution.model.QuizResponseDto;
import ru.dan.eduinstitution.model.QuizUpdateDto;
import ru.dan.eduinstitution.service.QuizService;

@Tag(name = "Quiz", description = "Operations related to quizzes")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @Operation(
            summary = "Create a new quiz",
            description = "Creates a new quiz with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quiz created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Module not found")
            }
    )
    @PostMapping
    public ResponseEntity<QuizResponseDto> createQuiz(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Quiz details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizCreateDto.class))
            ) @Valid @RequestBody QuizCreateDto dto) {
        log.info("Creating quiz with title: {}", dto.getTitle());
        QuizResponseDto responseDto = quizService.createQuiz(dto);
        log.info("Quiz created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get quiz by ID",
            description = "Retrieves a quiz by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Quiz not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDto> getQuizById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz")
            @PathVariable Long id) {
        log.info("Getting quiz by ID: {}", id);
        QuizResponseDto responseDto = quizService.getQuizById(id);
        log.info("Quiz retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update quiz",
            description = "Updates a quiz with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Quiz not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<QuizResponseDto> updateQuiz(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Quiz details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizUpdateDto.class))
            ) @Valid @RequestBody QuizUpdateDto dto) {
        log.info("Updating quiz with ID: {}", id);
        QuizResponseDto responseDto = quizService.updateQuiz(id, dto);
        log.info("Quiz updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete quiz",
            description = "Deletes a quiz by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Quiz deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Quiz not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the quiz")
            @PathVariable Long id) {
        log.info("Deleting quiz with ID: {}", id);
        quizService.deleteQuiz(id);
        log.info("Quiz deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get quiz by module ID",
            description = "Retrieves a quiz by its associated module ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuizResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Quiz not found for the given module")
            }
    )
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<QuizResponseDto> getQuizByModuleId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the module")
            @PathVariable Long moduleId) {
        log.info("Getting quiz for module with ID: {}", moduleId);
        QuizResponseDto responseDto = quizService.getQuizByModuleId(moduleId);
        if (responseDto != null) {
            log.info("Quiz retrieved with ID: {}", responseDto.getId());
            return ResponseEntity.ok(responseDto);
        } else {
            log.info("No quiz found for module with ID: {}", moduleId);
            return ResponseEntity.notFound().build();
        }
    }
}