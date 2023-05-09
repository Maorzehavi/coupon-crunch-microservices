package com.maorzehavi.categoryservice.controller;

import com.maorzehavi.categoryservice.model.dto.request.CategoryRequest;
import com.maorzehavi.categoryservice.model.dto.request.CategoryResponse;
import com.maorzehavi.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        var categoryResponse = categoryService.createCategory(categoryRequest).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category with name " + categoryRequest.getName() + " already exists"));
        return ResponseEntity.created(URI.create("/api/v1/categories/" + categoryResponse.getId()))
                .body(categoryResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.existsById(id));
    }

    @PostMapping("/test")
    public ResponseEntity<CategoryResponse> test(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.test(categoryRequest));
    }
}
