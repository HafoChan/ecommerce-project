package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.UserCreationRequest;
import com.sohan.user_service.dto.request.UserUpdateRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.UserResponse;
import com.sohan.user_service.exception.AppException;
import com.sohan.user_service.exception.ErrorCode;
import com.sohan.user_service.service.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    ApiResponse<UserResponse> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            UserResponse response = userService.getUserByUsername(username);
            return ApiResponse.<UserResponse>builder()
                    .success(true)
                    .result(response)
                    .build();
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
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
        boolean checkDelete = userService.deleteUser(userId);
        String messageKey = checkDelete ? "user.delete.success" : "user.delete.notExist";
        String message = messageSource.getMessage(messageKey, null, locale);

        return ApiResponse.<String>builder()
               .success(checkDelete)
               .message(message)
               .build();
    }
}
