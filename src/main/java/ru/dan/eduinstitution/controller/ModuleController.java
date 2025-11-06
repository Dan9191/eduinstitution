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
import ru.dan.eduinstitution.model.ModuleCreateDto;
import ru.dan.eduinstitution.model.ModuleResponseDto;
import ru.dan.eduinstitution.model.ModuleUpdateDto;
import ru.dan.eduinstitution.service.ModuleService;

import java.util.List;

@Tag(name = "Module", description = "Operations related to modules")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/module")
public class ModuleController {

    private final ModuleService moduleService;

    @Operation(
            summary = "Create a new module",
            description = "Creates a new module with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Module created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ModuleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    @PostMapping
    public ResponseEntity<ModuleResponseDto> createModule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Module details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModuleCreateDto.class))
            ) @Valid @RequestBody ModuleCreateDto dto) {
        log.info("Creating module with title: {}", dto.getTitle());
        ModuleResponseDto responseDto = moduleService.createModule(dto);
        log.info("Module created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get module by ID",
            description = "Retrieves a module by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Module retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ModuleResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Module not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ModuleResponseDto> getModuleById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the module")
            @PathVariable Long id) {
        log.info("Getting module by ID: {}", id);
        ModuleResponseDto responseDto = moduleService.getModuleById(id);
        log.info("Module retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update module",
            description = "Updates a module with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Module updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ModuleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Module not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ModuleResponseDto> updateModule(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the module")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Module details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModuleUpdateDto.class))
            ) @Valid @RequestBody ModuleUpdateDto dto) {
        log.info("Updating module with ID: {}", id);
        ModuleResponseDto responseDto = moduleService.updateModule(id, dto);
        log.info("Module updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete module",
            description = "Deletes a module by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Module deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Module not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the module")
            @PathVariable Long id) {
        log.info("Deleting module with ID: {}", id);
        moduleService.deleteModule(id);
        log.info("Module deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all modules for a course",
            description = "Retrieves all modules for a specific course",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of course modules retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ModuleResponseDto[].class))),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ModuleResponseDto>> getModulesByCourseId(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long courseId) {
        log.info("Getting modules for course with ID: {}", courseId);
        List<ModuleResponseDto> responseDtos = moduleService.getModulesByCourseId(courseId);
        log.info("Retrieved {} modules for course with ID: {}", responseDtos.size(), courseId);
        return ResponseEntity.ok(responseDtos);
    }
}