package ru.dan.eduinstitution.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.dan.eduinstitution.entity.Category;
import ru.dan.eduinstitution.model.CategoryResponseDto;
import ru.dan.eduinstitution.service.CategoryCacheService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

class CategoryControllerTest {

    private CategoryCacheService categoryCacheService;
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryCacheService = Mockito.mock(CategoryCacheService.class);
        categoryController = new CategoryController(categoryCacheService);
    }

    @Test
    @DisplayName("Тест получения всех категорий")
    void getAllCategories_ReturnsAllCategories() {
        // Given
        Map<Long, Category> categoriesMap = new HashMap<>();
        
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Programming");
        
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Web Development");
        
        categoriesMap.put(1L, category1);
        categoriesMap.put(2L, category2);
        
        Mockito.when(categoryCacheService.getCategoryMap()).thenReturn(categoriesMap);

        // When
        ResponseEntity<List<CategoryResponseDto>> response = categoryController.getAllCategories();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        
        CategoryResponseDto dto1 = response.getBody().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Programming", dto1.getName());
        
        CategoryResponseDto dto2 = response.getBody().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Web Development", dto2.getName());
        
        Mockito.verify(categoryCacheService).getCategoryMap();
    }

    @Test
    @DisplayName("Тест получения категории по существующему ID")
    void getCategoryById_WithValidId_ReturnsCategory() {
        // Given
        long validId = 1L;
        
        Category category = new Category();
        category.setId(validId);
        category.setName("Programming");
        
        Mockito.when(categoryCacheService.findById(anyInt())).thenReturn(category);

        // When
        ResponseEntity<CategoryResponseDto> response = categoryController.getCategoryById(validId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validId, response.getBody().getId());
        assertEquals("Programming", response.getBody().getName());
        
        Mockito.verify(categoryCacheService).findById(1);
    }

    @Test
    @DisplayName("Тест получения категории по несуществующему ID")
    void getCategoryById_WithInvalidId_ReturnsNotFound() {
        // Given
        long invalidId = 999L;
        
        Mockito.when(categoryCacheService.findById(anyInt())).thenReturn(null);

        // When
        ResponseEntity<CategoryResponseDto> response = categoryController.getCategoryById(invalidId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        Mockito.verify(categoryCacheService).findById(999);
    }
}