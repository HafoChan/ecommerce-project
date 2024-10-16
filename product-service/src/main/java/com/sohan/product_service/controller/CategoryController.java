package com.sohan.product_service.controller;

import com.sohan.product_service.dto.request.CategoryCreationAndUpdateRequest;
import com.sohan.product_service.dto.response.CategoryResponse;
import com.sohan.product_service.service.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    ICategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getProducts() {
        return categoryService.getAll();
    }

    @GetMapping("/{categoryId}")
    public CategoryResponse getProductsByCategory(@PathVariable Long categoryId) {
        return categoryService.getById(categoryId);
    }

    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryCreationAndUpdateRequest request) {
        return categoryService.create(request);
    }

    @PutMapping("/{categoryId}")
    public CategoryResponse updateCategory(@PathVariable Long categoryId, @RequestBody CategoryCreationAndUpdateRequest request) {
        return categoryService.update(categoryId, request);
    }

    @DeleteMapping("/{categoryId}")
    public boolean deleteCategory(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}
