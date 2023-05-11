package com.maorzehavi.categoryservice.controller;

import com.maorzehavi.categoryservice.model.entity.Category;
import com.maorzehavi.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategory(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + id + " not found")
        ));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestParam String name){
        var category = categoryService.create(name.toUpperCase()).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category with name " + name + " already exists")
        );
        return ResponseEntity.created(URI.create("/api/v1/categories/" + category.getId())).body(category);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Long id) {
        return categoryService.existsById(id);
    }
}
