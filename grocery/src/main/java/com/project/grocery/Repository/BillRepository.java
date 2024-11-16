package com.project.grocery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.grocery.Entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
}
