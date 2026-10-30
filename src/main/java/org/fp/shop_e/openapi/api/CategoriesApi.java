package org.fp.shop_e.openapi.api;

import jakarta.validation.Valid;
import org.fp.shop_e.dto.CategoryDtos.CategoryRequest;
import org.fp.shop_e.dto.CategoryDtos.CategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OpenAPI 3.0 Server Interface cho tài nguyên /api/v1/categories
 */
@RequestMapping("/api/v1/categories")
public interface CategoriesApi {

    @GetMapping
    ResponseEntity<List<CategoryResponse>> getCategories();

    @GetMapping("/{id}")
    ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id);

    @PostMapping
    ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request);

    @PutMapping("/{id}")
    ResponseEntity<CategoryResponse> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id);
}

