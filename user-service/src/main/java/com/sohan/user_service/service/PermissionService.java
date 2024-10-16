package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.PermissionRequest;
import com.sohan.user_service.dto.response.PermissionResponse;
import com.sohan.user_service.entity.PermissionEntity;
import com.sohan.user_service.mapper.PermissionMapper;
import com.sohan.user_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService{
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    @Transactional
    public PermissionResponse create(PermissionRequest request) {
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
    @Transactional
    public void delete(String name) {
        permissionRepository.deleteById(name);
    }
}
