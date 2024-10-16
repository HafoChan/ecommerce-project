package com.sohan.product_service.dto.response;

import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    long productId;
    String name;
    double price;
    String description;
    int quantity;
    List<CategoryResponse> categories;
    @Lob
    byte[] image;
}
