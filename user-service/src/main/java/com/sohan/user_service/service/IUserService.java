package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.UserCreationRequest;
import com.sohan.user_service.dto.request.UserUpdateRequest;
import com.sohan.user_service.dto.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse register(UserCreationRequest request);

    UserResponse getUserByUsername(String username);

    List<UserResponse> getAllUsers(Integer pageNumber, Integer size, String sortBy);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    boolean deleteUser(String userId);
}
