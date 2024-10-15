package com.sohan.user_service.repository;

import com.sohan.user_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, PagingAndSortingRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
}
