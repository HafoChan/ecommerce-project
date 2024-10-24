package com.sohan.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
public class RoleEntity {

    @Id
    String name;
    String description;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<PermissionEntity> permissions;
}