package com.maorzehavi.companyservice.repository;

import com.maorzehavi.companyservice.model.entity.Company;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);

    boolean existsByName(String name);

    @Query("select c.id from Company c where c.userId = ?1")
    Optional<Long> findIdByUserId(Long userId);
}
