package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.PermissionRequest;
import com.sohan.user_service.dto.response.PermissionResponse;
import com.sohan.user_service.entity.PermissionEntity;
import com.sohan.user_service.entity.RoleEntity;
import com.sohan.user_service.exception.AppException;
import com.sohan.user_service.exception.ErrorCode;
import com.sohan.user_service.mapper.PermissionMapper;
import com.sohan.user_service.repository.PermissionRepository;
import com.sohan.user_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService{
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public PermissionResponse create(PermissionRequest request) {
        if (permissionRepository.existsById(request.getName()))
            throw new AppException(ErrorCode.PERMISSION_EXISTS);

        PermissionEntity permission = permissionMapper.toPermissionEntity(request);
        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
        List<PermissionEntity> permissions = permissionRepository.findAll();

        return permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @Override
    public PermissionResponse getById(String name) {
        PermissionEntity permission = permissionRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXIST));
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    @Transactional
    public boolean delete(String name) {

        PermissionEntity permission = permissionRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXIST));

        List<RoleEntity> roles = roleRepository.findAll();
        for (RoleEntity role : roles) {
            role.getPermissions().remove(permission);
        }
        roleRepository.saveAll(roles);

        permissionRepository.delete(permission);
        return true;
    }


}
