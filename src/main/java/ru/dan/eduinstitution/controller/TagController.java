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
import ru.dan.eduinstitution.model.TagCreateDto;
import ru.dan.eduinstitution.model.TagResponseDto;
import ru.dan.eduinstitution.model.TagUpdateDto;
import ru.dan.eduinstitution.service.TagService;

import java.util.List;
import java.util.Set;

@Tag(name = "Tag", description = "Operations related to tags")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @Operation(
            summary = "Create a new tag",
            description = "Creates a new tag with the provided name",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tag created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TagResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "Tag with this name already exists")
            }
    )
    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Tag details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagCreateDto.class))
            ) @Valid @RequestBody TagCreateDto dto) {
        log.info("Creating tag with name: {}", dto.getName());
        TagResponseDto responseDto = tagService.createTag(dto);
        log.info("Tag created with ID: {}", responseDto.getId());
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(
            summary = "Get tag by ID",
            description = "Retrieves a tag by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tag retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TagResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Tag not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDto> getTagById(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the tag")
            @PathVariable Long id) {
        log.info("Getting tag by ID: {}", id);
        TagResponseDto responseDto = tagService.getTagById(id);
        log.info("Tag retrieved with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update tag",
            description = "Updates a tag with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tag updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TagResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Tag not found"),
                    @ApiResponse(responseCode = "409", description = "Tag with this name already exists")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDto> updateTag(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the tag")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Tag details for update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagUpdateDto.class))
            ) @Valid @RequestBody TagUpdateDto dto) {
        log.info("Updating tag with ID: {}", id);
        TagResponseDto responseDto = tagService.updateTag(id, dto);
        log.info("Tag updated with ID: {}", responseDto.getId());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete tag",
            description = "Deletes a tag by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Tag not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the tag")
            @PathVariable Long id) {
        log.info("Deleting tag with ID: {}", id);
        tagService.deleteTag(id);
        log.info("Tag deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all tags",
            description = "Retrieves all tags",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tags retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TagResponseDto[].class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        log.info("Getting all tags");
        List<TagResponseDto> responseDtos = tagService.getAllTags();
        log.info("Retrieved {} tags", responseDtos.size());
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "Add tag to course",
            description = "Adds a tag to a specific course",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tag added to course successfully"),
                    @ApiResponse(responseCode = "404", description = "Course or tag not found")
            }
    )
    @PostMapping("/course/{courseId}/tag/{tagId}")
    public ResponseEntity<Void> addTagToCourse(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long courseId,
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the tag")
            @PathVariable Long tagId) {
        log.info("Adding tag ID {} to course ID {}", tagId, courseId);
        tagService.addTagToCourse(courseId, tagId);
        log.info("Tag ID {} added to course ID {}", tagId, courseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remove tag from course",
            description = "Removes a tag from a specific course",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tag removed from course successfully"),
                    @ApiResponse(responseCode = "404", description = "Course or tag not found")
            }
    )
    @DeleteMapping("/course/{courseId}/tag/{tagId}")
    public ResponseEntity<Void> removeTagFromCourse(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long courseId,
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the tag")
            @PathVariable Long tagId) {
        log.info("Removing tag ID {} from course ID {}", tagId, courseId);
        tagService.removeTagFromCourse(courseId, tagId);
        log.info("Tag ID {} removed from course ID {}", tagId, courseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add multiple tags to course",
            description = "Adds multiple tags to a specific course",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tags added to course successfully"),
                    @ApiResponse(responseCode = "404", description = "Course or one of the tags not found")
            }
    )
    @PostMapping("/course/{courseId}/tags")
    public ResponseEntity<Void> addTagsToCourse(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long courseId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of tag IDs to add",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Set.class))
            ) @RequestBody Set<Long> tagIds) {
        log.info("Adding tags {} to course ID {}", tagIds, courseId);
        tagService.addTagsToCourse(courseId, tagIds);
        log.info("Tags {} added to course ID {}", tagIds, courseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remove multiple tags from course",
            description = "Removes multiple tags from a specific course",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tags removed from course successfully"),
                    @ApiResponse(responseCode = "404", description = "Course or one of the tags not found")
            }
    )
    @DeleteMapping("/course/{courseId}/tags")
    public ResponseEntity<Void> removeTagsFromCourse(
            @io.swagger.v3.oas.annotations.Parameter(description = "Unique identifier of the course")
            @PathVariable Long courseId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of tag IDs to remove",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Set.class))
            ) @RequestBody Set<Long> tagIds) {
        log.info("Removing tags {} from course ID {}", tagIds, courseId);
        tagService.removeTagsFromCourse(courseId, tagIds);
        log.info("Tags {} removed from course ID {}", tagIds, courseId);
        return ResponseEntity.noContent().build();
    }
}