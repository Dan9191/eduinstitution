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
import ru.dan.eduinstitution.model.AnswerOptionCreateDto;
import ru.dan.eduinstitution.model.AnswerOptionResponseDto;
import ru.dan.eduinstitution.model.AnswerOptionUpdateDto;
import ru.dan.eduinstitution.service.AnswerOptionService;

import java.util.List;

@Tag(name = "AnswerOption", description = "Operations related to answer options")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/answer-option")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;

    @Operation(
            summary = "Create a new answer option",
            description = "Creates a new answer option with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Answer option created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnswerOptionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Question not found")
            }
    )
    @PostMapping
    public ResponseEntity<AnswerOptionResponseDto> createAnswerOption(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Answer option details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerOptionCreateDto.class))
            ) @Valid @RequestBody AnswerOptionCreateDto dto) {
        log.info("Creating answer option with text: {}", dto.getText());
        AnswerOptionResponseDto responseDto = answerOptionService.createAnswerOption(dto);
        log.info("Answer option created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get answer option by ID",
            description = "Retrieves an answer option by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Answer option retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnswerOptionResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Answer option not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnswerOptionResponseDto> getAnswerOptionById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the answer option")
            @PathVariable Long id) {
        log.info("Getting answer option by ID: {}", id);
        AnswerOptionResponseDto responseDto = answerOptionService.getAnswerOptionById(id);
        log.info("Answer option retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update answer option",
            description = "Updates an answer option with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Answer option updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnswerOptionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Answer option not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AnswerOptionResponseDto> updateAnswerOption(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the answer option")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Answer option details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerOptionUpdateDto.class))
            ) @Valid @RequestBody AnswerOptionUpdateDto dto) {
        log.info("Updating answer option with ID: {}", id);
        AnswerOptionResponseDto responseDto = answerOptionService.updateAnswerOption(id, dto);
        log.info("Answer option updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete answer option",
            description = "Deletes an answer option by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Answer option deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Answer option not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswerOption(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the answer option")
            @PathVariable Long id) {
        log.info("Deleting answer option with ID: {}", id);
        answerOptionService.deleteAnswerOption(id);
        log.info("Answer option deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all answer options for a question",
            description = "Retrieves all answer options for a specific question",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of question answer options retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnswerOptionResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Question not found")
            }
    )
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerOptionResponseDto>> getAnswerOptionsByQuestionId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the question")
            @PathVariable Long questionId) {
        log.info("Getting answer options for question with ID: {}", questionId);
        List<AnswerOptionResponseDto> responseDtos = answerOptionService.getAnswerOptionsByQuestionId(questionId);
        log.info("Retrieved {} answer options for question with ID: {}", responseDtos.size(), questionId);
        return ResponseEntity.ok(responseDtos);
    }
}