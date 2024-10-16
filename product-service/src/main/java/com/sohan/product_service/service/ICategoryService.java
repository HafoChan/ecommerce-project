package com.sohan.product_service.service;

import com.sohan.product_service.dto.request.CategoryCreationAndUpdateRequest;
import com.sohan.product_service.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getAll();

    CategoryResponse getById(Long categoryId);

    CategoryResponse create(CategoryCreationAndUpdateRequest request);

    boolean delete(Long categoryId);

    CategoryResponse update(Long categoryId, CategoryCreationAndUpdateRequest request);
}
