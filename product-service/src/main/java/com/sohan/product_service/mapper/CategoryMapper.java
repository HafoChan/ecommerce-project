package com.sohan.product_service.mapper;

import com.sohan.product_service.dto.request.CategoryCreationAndUpdateRequest;
import com.sohan.product_service.dto.response.CategoryResponse;
import com.sohan.product_service.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(CategoryEntity category);

    CategoryEntity toCategoryEntity(CategoryCreationAndUpdateRequest request);
}
