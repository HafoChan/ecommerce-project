package com.sohan.user_service.security;

import com.sohan.user_service.entity.PermissionEntity;
import com.sohan.user_service.entity.RoleEntity;
import com.sohan.user_service.entity.UserEntity;
import com.sohan.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);

        Set<RoleEntity> roles = user.getRoles();

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            Set<PermissionEntity> permissions = role.getPermissions();

            for (PermissionEntity permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
            System.out.println("................................");
        }

        return new User(
            user.getUsername(),
            user.getPassword(),
            authorities
        );
    }
}
