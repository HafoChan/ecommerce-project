package com.sohan.product_service.controller;

import com.sohan.product_service.dto.response.ProductResponse;
import com.sohan.product_service.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    IProductService productService;

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProductById(@PathVariable Long productId) {
        return productService.getById(productId);
    }

    @PostMapping
    public ProductResponse createProduct(@RequestParam String name,
                                         @RequestParam double price,
                                         @RequestParam String description,
                                         @RequestParam int quantity,
                                         @RequestParam("image") MultipartFile image,
                                         @RequestParam String categories) {
        try {
            byte[] imageBytes = image.getBytes();
            return productService.createProduct(name, price, description, quantity, imageBytes, categories);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(@PathVariable Long productId,
                                         @RequestParam String name,
                                         @RequestParam double price,
                                         @RequestParam String description,
                                         @RequestParam int quantity,
                                         @RequestParam("image") MultipartFile image,
                                         @RequestParam String categories) {
        try {
            byte[] imageBytes = image.getBytes();
            return productService.updateProduct(productId, name, price, description, quantity, imageBytes, categories);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    public boolean deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }
}
