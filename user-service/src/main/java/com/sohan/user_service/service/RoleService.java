package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.RoleRequest;
import com.sohan.user_service.dto.response.RoleResponse;
import com.sohan.user_service.entity.PermissionEntity;
import com.sohan.user_service.entity.RoleEntity;
import com.sohan.user_service.entity.UserEntity;
import com.sohan.user_service.exception.AppException;
import com.sohan.user_service.exception.ErrorCode;
import com.sohan.user_service.mapper.RoleMapper;
import com.sohan.user_service.repository.PermissionRepository;
import com.sohan.user_service.repository.RoleRepository;
import com.sohan.user_service.repository.UserRepository;
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
    UserRepository userRepository;
    RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsById(request.getName()))
            throw new AppException(ErrorCode.ROLE_EXISTS);
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
    public RoleResponse getById(String name) {
        RoleEntity role = roleRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));
        return roleMapper.toRoleResponse(role);
    }

    @Override
    @Transactional
    public boolean delete(String name) {

        RoleEntity role = roleRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        role.getPermissions().clear();
        roleRepository.save(role);

        List<UserEntity> users = userRepository.findAllByRolesContaining(role);
        for (UserEntity user : users) {
            user.getRoles().remove(role);
        }
        userRepository.saveAll(users);

        roleRepository.delete(role);
        return true;
    }
}
