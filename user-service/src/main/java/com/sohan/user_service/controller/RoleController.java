package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.RoleRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.RoleResponse;
import com.sohan.user_service.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    IRoleService roleService;
    MessageSource messageSource;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .success(true)
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .success(true)
                .result(roleService.getAll())
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .success(true)
                .result(roleService.getById(name))
                .build();
    }

    @DeleteMapping("/{role}")
    public ApiResponse<Boolean> deleteRole(@PathVariable String role, Locale locale) {
        boolean checkDelete = roleService.delete(role);
        String messageKey = checkDelete ? "role.delete.success" : "role.delete.notExist";
        String message = messageSource.getMessage(messageKey, null, locale);
        return ApiResponse.<Boolean>builder()
                .success(checkDelete)
                .message(message)
                .build();
    }
}
