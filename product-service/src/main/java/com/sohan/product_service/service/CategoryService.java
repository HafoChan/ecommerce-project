package com.sohan.product_service.service;

import com.sohan.product_service.dto.request.CategoryCreationAndUpdateRequest;
import com.sohan.product_service.dto.response.CategoryResponse;
import com.sohan.product_service.entity.CategoryEntity;
import com.sohan.product_service.entity.ProductEntity;
import com.sohan.product_service.mapper.CategoryMapper;
import com.sohan.product_service.mapper.ProductMapper;
import com.sohan.product_service.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService{

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    ProductMapper productMapper;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
    }

    @Override
    public CategoryResponse getById(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<ProductEntity> listProducts = category.getProducts();
        category.setProducts(listProducts);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryCreationAndUpdateRequest request) {
        CategoryEntity category = categoryMapper.toCategoryEntity(request);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long categoryId, CategoryCreationAndUpdateRequest request) {
        if (categoryRepository.existsById(categoryId)) {
            CategoryEntity category = categoryRepository.findById(categoryId).get();
            category.setCategoryName(request.getCategoryName());
            category.setDescription(request.getDescription());
            categoryRepository.save(category);
            return categoryMapper.toCategoryResponse(category);
        }
        throw new RuntimeException("Category not found");
    }

    @Override
    @Transactional
    public boolean delete(Long categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
