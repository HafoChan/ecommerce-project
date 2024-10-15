package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.RoleRequest;
import com.sohan.user_service.dto.response.RoleResponse;
import com.sohan.user_service.entity.PermissionEntity;
import com.sohan.user_service.entity.RoleEntity;
import com.sohan.user_service.mapper.RoleMapper;
import com.sohan.user_service.repository.PermissionRepository;
import com.sohan.user_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService{
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        List<PermissionEntity> permissions = permissionRepository.findAllById(request.getPermissions());

        RoleEntity role = roleMapper.toRoleEntity(request);
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String name) {
        roleRepository.deleteById(name);
    }
}
