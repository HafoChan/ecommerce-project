package com.sohan.user_service.mapper;

import com.sohan.user_service.dto.request.RoleRequest;
import com.sohan.user_service.dto.response.RoleResponse;
import com.sohan.user_service.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    RoleEntity toRoleEntity(RoleRequest request);

    RoleResponse toRoleResponse(RoleEntity role);
}
