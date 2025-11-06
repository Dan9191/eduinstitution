package ru.dan.eduinstitution.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Category;
import ru.dan.eduinstitution.repository.CategoryRepository;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис, кеширующий данные по категориям.
 */
@Service
@RequiredArgsConstructor
public class CategoryCacheService {

    private final CategoryRepository categoryRepository;

    @Getter
    private Map<Long, Category> categoryMap;

    @PostConstruct
    void init() {
        categoryMap = categoryRepository.findAll().stream().collect(Collectors.toMap(Category::getId, Function.identity()));
    }

    public Category findById(int id) {
        return categoryMap.get((long) id);
    }
}