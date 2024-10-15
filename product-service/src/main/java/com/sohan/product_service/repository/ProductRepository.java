package com.sohan.product_service.repository;

import com.sohan.product_service.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>,
        PagingAndSortingRepository<ProductEntity, Long> {
}
