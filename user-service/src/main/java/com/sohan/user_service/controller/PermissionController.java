package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.PermissionRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.PermissionResponse;
import com.sohan.user_service.service.IPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    IPermissionService permissionService;
    MessageSource messageSource;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .success(true)
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .success(true)
                .result(permissionService.getAll())
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<PermissionResponse> getPermission(@PathVariable String name) {
        return ApiResponse.<PermissionResponse>builder()
                .success(true)
                .result(permissionService.getById(name))
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Boolean> deletePermission(@PathVariable String permission, Locale locale) {
        boolean checkDelete = permissionService.delete(permission);
        String messageKey = checkDelete ? "permission.delete.success" : "permission.delete.notExist";
        String message = messageSource.getMessage(messageKey, null, locale);
        return ApiResponse.<Boolean>builder()
                .success(checkDelete)
                .message(message)
                .build();
    }
}
