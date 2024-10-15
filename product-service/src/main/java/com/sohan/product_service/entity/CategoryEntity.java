package com.sohan.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long categoryId;
    String categoryName;

    @OneToMany
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
    List<ProductEntity> products;
}
