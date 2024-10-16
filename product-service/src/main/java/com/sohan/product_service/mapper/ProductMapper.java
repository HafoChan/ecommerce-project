package com.sohan.product_service.mapper;

import com.sohan.product_service.dto.response.ProductResponse;
import com.sohan.product_service.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(ProductEntity product);
}
