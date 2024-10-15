package com.sohan.user_service.mapper;

import com.sohan.user_service.dto.request.UserCreationRequest;
import com.sohan.user_service.dto.request.UserUpdateRequest;
import com.sohan.user_service.dto.response.UserResponse;
import com.sohan.user_service.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserCreationRequest request);

    UserResponse toUserResponse(UserEntity user);

    void updateUserEntity(@MappingTarget UserEntity user, UserUpdateRequest request);
}
