package com.project.grocery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.grocery.Entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
