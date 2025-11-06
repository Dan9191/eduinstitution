package ru.dan.eduinstitution.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import ru.dan.eduinstitution.config.BaseTestWithContext;
import ru.dan.eduinstitution.entity.Category;
import ru.dan.eduinstitution.model.CategoryResponseDto;
import ru.dan.eduinstitution.repository.CategoryRepository;
import ru.dan.eduinstitution.service.CategoryCacheService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для CategoryController.
 * Использует настоящую базу данных через Testcontainers.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryIntegrationTest extends BaseTestWithContext {

    @Autowired
    private CategoryCacheService categoryCacheService;

    @BeforeEach
    void setUp() {
        categoryCacheService.updateCache();
    }

    @Test
    @DisplayName("Тест получения всех категорий через REST API")
    void getAllCategoriesViaRestApi_ReturnsAllCategories() {
        // When
        ResponseEntity<CategoryResponseDto[]> response = restTemplate.getForEntity("/category", CategoryResponseDto[].class);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Проверяем, что возвращаются все категории из миграции
        assertEquals(15, response.getBody().length);

        CategoryResponseDto[] categories = response.getBody();
        List<String> names = List.of(
            categories[0].getName(), 
            categories[1].getName(),
            categories[2].getName()
        );
        assertTrue(names.contains("Программирование"));
        assertTrue(names.contains("Веб-разработка"));
        assertTrue(names.contains("Мобильная разработка"));
    }

    @Test
    @DisplayName("Тест получения категории по существующему ID через REST API")
    void getCategoryByIdViaRestApi_WithValidId_ReturnsCategory() {
        // Given
        long categoryId = 1L; // Программирование из миграции
        String expectedName = "Программирование";

        // When
        ResponseEntity<CategoryResponseDto> response = restTemplate.getForEntity("/category/{id}", CategoryResponseDto.class, categoryId);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(categoryId, response.getBody().getId());
        assertEquals(expectedName, response.getBody().getName());
    }

    @Test
    @DisplayName("Тест получения категории по несуществующему ID через REST API")
    void getCategoryByIdViaRestApi_WithInvalidId_ReturnsNotFound() {
        // When
        ResponseEntity<CategoryResponseDto> response = restTemplate.getForEntity("/category/{id}", CategoryResponseDto.class, 999L);

        // Then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}