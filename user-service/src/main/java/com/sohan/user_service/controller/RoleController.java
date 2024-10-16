package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.RoleRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.RoleResponse;
import com.sohan.user_service.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    IRoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Role created successfully")
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .success(true)
                .message("Roles retrieved successfully")
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    public ApiResponse<Boolean> deleteRole(@PathVariable String role) {
        try {
            roleService.delete(role);
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
