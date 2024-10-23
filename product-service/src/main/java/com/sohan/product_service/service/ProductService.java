package com.sohan.product_service.service;

import com.sohan.product_service.dto.response.ProductResponse;
import com.sohan.product_service.entity.CategoryEntity;
import com.sohan.product_service.entity.ProductEntity;
import com.sohan.product_service.exception.AppException;
import com.sohan.product_service.exception.ErrorCode;
import com.sohan.product_service.mapper.CategoryMapper;
import com.sohan.product_service.mapper.ProductMapper;
import com.sohan.product_service.repository.CategoryRepository;
import com.sohan.product_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    CategoryMapper categoryMapper;

    @Override
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public ProductResponse getById(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(String name, double price,
                                         String description, int quantity, byte[] image,
                                         String categories) {

        List<Long> categoryIds = Arrays.stream(categories.split(","))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty() && s.matches("\\d+"))
                                        .map(Long::valueOf)
                                        .toList();

        List<CategoryEntity> categoriesEntity = categoryRepository.findAllById(categoryIds);

        ProductEntity product = ProductEntity.builder()
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .image(image)
                .categories(categoriesEntity)
                .build();
        productRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId,
                                         String name, double price,
                                         String description, int quantity, byte[] image,
                                         String categories) {
        if (productRepository.existsById(productId)) {
            List<Long> categoryIds = Arrays.stream(categories.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty() && s.matches("\\d+"))
                    .map(Long::valueOf)
                    .toList();

            List<CategoryEntity> categoriesEntity = categoryRepository.findAllById(categoryIds);

            ProductEntity product = productRepository.findById(productId).get();
            product.setName(name);
            product.setPrice(price);
            product.setCategories(categoriesEntity);
            product.setDescription(description);
            product.setQuantity(quantity);
            product.setImage(image);

            productRepository.save(product);

            return productMapper.toProductResponse(product);
        }
        throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long productId) {
        try {
            productRepository.deleteById(productId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
