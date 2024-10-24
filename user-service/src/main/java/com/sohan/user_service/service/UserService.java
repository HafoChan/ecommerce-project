package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.UserCreationRequest;
import com.sohan.user_service.dto.request.UserUpdateRequest;
import com.sohan.user_service.dto.response.UserResponse;
import com.sohan.user_service.entity.RoleEntity;
import com.sohan.user_service.entity.UserEntity;
import com.sohan.user_service.enums.Role;
import com.sohan.user_service.exception.AppException;
import com.sohan.user_service.exception.ErrorCode;
import com.sohan.user_service.mapper.UserMapper;
import com.sohan.user_service.repository.RoleRepository;
import com.sohan.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponse register(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTS);
        }

        UserEntity user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<RoleEntity> roles = new HashSet<>();
        RoleEntity role = roleRepository.findById(Role.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Tới đây rồi nè ....................................");
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserEntity user = userRepository.findByUsername(username);
            if (user == null) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
            return userMapper.toUserResponse(user);
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @Override
    public List<UserResponse> getAllUsers(Integer pageNumber, Integer size, String sortBy) {

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, sortBy));

        Page<UserEntity> pageResult = userRepository.findAll(pageable);
        if (pageResult.hasContent())
            return pageResult.getContent().stream().map(userMapper::toUserResponse).toList();
        return List.of();
    }

    @Override
    @Transactional
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        checkAuthorizationUser(user.getUsername());

        userMapper.updateUserEntity(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        checkAuthorizationUser(user.getUsername());
        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserById(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    private void checkAuthorizationUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !currentUsername.equals(username)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
