package com.sohan.user_service.mapper;

import com.sohan.user_service.dto.request.PermissionRequest;
import com.sohan.user_service.dto.response.PermissionResponse;
import com.sohan.user_service.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionEntity toPermissionEntity(PermissionRequest request);

    PermissionResponse toPermissionResponse(PermissionEntity entity);
}
