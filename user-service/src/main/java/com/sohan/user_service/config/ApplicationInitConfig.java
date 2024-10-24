package com.sohan.user_service.config;

import com.sohan.user_service.entity.PermissionEntity;
import com.sohan.user_service.entity.RoleEntity;
import com.sohan.user_service.entity.UserEntity;
import com.sohan.user_service.enums.Role;
import com.sohan.user_service.repository.PermissionRepository;
import com.sohan.user_service.repository.RoleRepository;
import com.sohan.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.ObjectUtils.isEmpty;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            initializePermissionsAdmin(permissionRepository);
            initializeRoles(roleRepository, permissionRepository);
            initializeAdminUser(userRepository, roleRepository, passwordEncoder);
        };
    }

    @Transactional
    void initializePermissionsAdmin(PermissionRepository permissionRepository) {
        if (permissionRepository.findAll().isEmpty()) {
            PermissionEntity readPermission = new PermissionEntity("READ_PRIVILEGES", "Grants read access");
            PermissionEntity writePermission = new PermissionEntity("WRITE_PRIVILEGES", "Grants write access");
            PermissionEntity deletePermission = new PermissionEntity("DELETE_PRIVILEGES", "Grants delete access");

            permissionRepository.save(readPermission);
            permissionRepository.save(writePermission);
            permissionRepository.save(deletePermission);
        }
    }

    @Transactional
    void initializeRoles(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        if (roleRepository.findAll().isEmpty()) {
            Set<PermissionEntity> adminPermissions = new HashSet<>(permissionRepository.findAll());

            RoleEntity adminRole = new RoleEntity();
            adminRole.setName(Role.ADMIN.name());
            adminRole.setDescription("Admin role");
            adminRole.setPermissions(adminPermissions);

            roleRepository.save(adminRole);

            initializePermissionsAndRoleUser(permissionRepository);
        }
    }

    @Transactional
    void initializeAdminUser(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        if (isEmpty(userRepository.findByUsername("admin"))) {
            RoleEntity adminRole = roleRepository.findById(Role.ADMIN.name())
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            RoleEntity userRole = roleRepository.findById(Role.USER.name())
                    .orElseThrow(() -> new RuntimeException("User role not found"));

            Set<RoleEntity> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);

            UserEntity adminUser = UserEntity.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(roles)
                    .build();

            userRepository.save(adminUser);
            log.warn("Admin user has been created");
        }
    }

    void initializePermissionsAndRoleUser(PermissionRepository permissionRepository) {
        PermissionEntity readUserPermission = new PermissionEntity("READ_USER_PRIVILEGES", "Grants read access for user data");
        PermissionEntity updateUserPermission = new PermissionEntity("UPDATE_USER_PRIVILEGES", "Grants update access for user data");
        PermissionEntity deleteUserPermission = new PermissionEntity("DELETE_USER_PRIVILEGES", "Grants delete access for user data");

        permissionRepository.save(readUserPermission);
        permissionRepository.save(updateUserPermission);
        permissionRepository.save(deleteUserPermission);

        Set<PermissionEntity> userPermissions = new HashSet<>();
        userPermissions.add(readUserPermission);
        userPermissions.add(updateUserPermission);
        userPermissions.add(deleteUserPermission);

        RoleEntity userRole = new RoleEntity();
        userRole.setName(Role.USER.name());
        userRole.setDescription("User role");
        userRole.setPermissions(userPermissions);

        roleRepository.save(userRole);
    }
}