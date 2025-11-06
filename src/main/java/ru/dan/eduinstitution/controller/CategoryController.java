package ru.dan.eduinstitution.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dan.eduinstitution.entity.Category;
import ru.dan.eduinstitution.model.CategoryResponseDto;
import ru.dan.eduinstitution.service.CategoryCacheService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Category", description = "Operations related to categories")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryCacheService categoryCacheService;

    @Operation(
        summary = "Get all categories",
        description = "Retrieves all available categories",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = CategoryResponseDto.class)))
        }
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        log.info("Retrieving all categories from cache");
        List<Category> categories = categoryCacheService.getCategoryMap().values().stream()
                .collect(Collectors.toList());
        
        List<CategoryResponseDto> dtos = categories.stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
        
        log.info("Retrieved {} categories", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @Operation(
        summary = "Get category by ID",
        description = "Retrieves a category by its unique ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(
        @Parameter(description = "Unique identifier of the category", required = true) 
        @PathVariable Long id) {
        log.info("Retrieving category by ID: {}", id);
        Category category = categoryCacheService.findById(id.intValue());
        if (category != null) {
            CategoryResponseDto dto = new CategoryResponseDto(category.getId(), category.getName());
            log.info("Successfully retrieved category: {}", dto.getName());
            return ResponseEntity.ok(dto);
        } else {
            log.warn("Category not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}