package com.project.grocery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.grocery.Entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}




