package com.maorzehavi.categoryservice.service;

import com.maorzehavi.categoryservice.model.dto.request.CategoryRequest;
import com.maorzehavi.categoryservice.model.dto.request.CategoryResponse;
import com.maorzehavi.categoryservice.model.entity.Category;
import com.maorzehavi.categoryservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<CategoryResponse> findById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToResponse);
    }

    public Optional<CategoryResponse> createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryRequest.getName() + " already exists");
        }
        Category category = mapToEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(category);
        return Optional.of(mapToResponse(savedCategory));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private Category mapToEntity(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName())
                .build();
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
