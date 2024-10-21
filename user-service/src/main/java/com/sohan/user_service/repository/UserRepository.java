package com.sohan.user_service.repository;

import com.sohan.user_service.entity.RoleEntity;
import com.sohan.user_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, PagingAndSortingRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<UserEntity> findAllByRolesContaining(RoleEntity role);
}
