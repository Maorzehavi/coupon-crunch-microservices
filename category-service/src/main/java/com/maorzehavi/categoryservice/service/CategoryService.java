package com.maorzehavi.categoryservice.service;

import com.maorzehavi.categoryservice.model.entity.Category;
import com.maorzehavi.categoryservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate;

    public Optional<Category> create(String name) {
        if (existsByName(name)) {
            return Optional.empty();
        }
        Category category = Category.builder()
                .name(name)
                .build();
        return Optional.of(categoryRepository.save(category));
    }

    public Optional<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public boolean deleteCategory(Long id) {
        boolean exists = Boolean.TRUE.equals(restTemplate.getForObject("http://coupon-service/api/v1/coupons/category/exists/" + id, Boolean.class));
        if (exists) {
            throw new RuntimeException("Cannot delete category with coupons attached");
        }

        if (existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        throw new RuntimeException("Category with id " + id + " not found");
    }

    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }


    private boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }


}
