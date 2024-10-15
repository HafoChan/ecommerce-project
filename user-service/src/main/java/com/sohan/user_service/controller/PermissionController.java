package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.PermissionRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.PermissionResponse;
import com.sohan.user_service.service.IPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    IPermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .success(true)
                .message("Permission created successfully")
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .success(true)
                .message("Permissions retrieved successfully")
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Boolean> deletePermission(@PathVariable String permission) {
        try {
            permissionService.delete(permission);
            return ApiResponse.<Boolean>builder()
                    .success(true)
                    .message("Permissions deleted successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Boolean>builder()
                    .success(false)
                    .message("Error deleting permission: " + e.getMessage())
                    .build();
        }
    }
}
