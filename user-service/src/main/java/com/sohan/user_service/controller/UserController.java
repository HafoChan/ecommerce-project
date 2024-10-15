package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.UserCreationRequest;
import com.sohan.user_service.dto.request.UserUpdateRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.UserResponse;
import com.sohan.user_service.service.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    IUserService userService;

    @PostMapping
    ApiResponse<UserResponse> register(@RequestBody @Valid UserCreationRequest request) {
        try {
            UserResponse response = userService.register(request);
            return ApiResponse.<UserResponse>builder()
                    .success(true)
                    .message("User registered successfully")
                    .result(response)
                   .build();
        } catch (Exception e) {
            return ApiResponse.<UserResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();

                UserResponse response = userService.getUserByUsername(username);
                return ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User retrieved successfully")
                        .result(response)
                        .build();
            } else {
                throw new RuntimeException("User is not authenticated");
            }
        } catch (Exception e) {
            return ApiResponse.<UserResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "username") String sortBy) {
        try {
            List<UserResponse> response = userService.getAllUsers(pageNumber - 1, size, sortBy);
            return ApiResponse.<List<UserResponse>>builder()
                    .success(true)
                    .message("User retrieved successfully")
                    .result(response)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<UserResponse>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }


    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        try {
            UserResponse response = userService.updateUser(userId, request);
            return ApiResponse.<UserResponse>builder()
                    .success(true)
                    .message("User updated successfully")
                    .result(response)
                   .build();
        } catch (Exception e) {
            return ApiResponse.<UserResponse>builder()
                   .success(false)
                   .message(e.getMessage())
                   .build();
        }
    }
}
