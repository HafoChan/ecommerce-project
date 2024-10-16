package com.sohan.product_service.service;

import com.sohan.product_service.dto.response.ProductResponse;

import java.util.List;

public interface IProductService {

    List<ProductResponse> getProducts();

    ProductResponse getById(Long productId);

    ProductResponse createProduct(String name, double price, String description,
                                  int quantity, byte[] image, String categories);

    ProductResponse updateProduct(Long productId, String name, double price, String description,
                                  int quantity, byte[] image, String categories);

    boolean deleteProduct(Long productId);
}
