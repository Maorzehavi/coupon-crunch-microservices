package com.maorzehavi.customerservice.repository;

import com.maorzehavi.customerservice.model.entity.Customer;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    @Query("select c.id from Customer c where c.userId = ?1")
    Optional<Long> findIdByUserId(Long userId);
}