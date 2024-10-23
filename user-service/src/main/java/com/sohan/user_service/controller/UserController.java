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
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    IUserService userService;
    MessageSource messageSource;

    @PostMapping
    ApiResponse<UserResponse> register(@RequestBody @Valid UserCreationRequest request) {
        UserResponse response = userService.register(request);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .result(response)
               .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        System.out.println("Tới endpoint lấy info");
        UserResponse response = userService.getMyInfo();
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable("userId") String userId) {
        UserResponse response = userService.getUserById(userId);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .result(response)
               .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers(@RequestParam(defaultValue = "1") Integer pageNumber,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(defaultValue = "username") String sortBy) {
        List<UserResponse> response = userService.getAllUsers(pageNumber - 1, size, sortBy);
        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .result(response)
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(userId, request);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse <String> deleteUser(@PathVariable String userId, Locale locale) {
        userService.deleteUser(userId);
        String messageKey = "user.delete.success";
        String message = messageSource.getMessage(messageKey, null, locale);

        return ApiResponse.<String>builder()
               .success(true)
               .message(message)
               .build();
    }
}
