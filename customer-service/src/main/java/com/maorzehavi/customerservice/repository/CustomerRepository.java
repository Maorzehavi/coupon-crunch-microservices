package com.maorzehavi.customerservice.repository;

import com.maorzehavi.customerservice.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}