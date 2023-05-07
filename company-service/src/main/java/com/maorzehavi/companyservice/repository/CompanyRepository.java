package com.maorzehavi.companyservice.repository;

import com.maorzehavi.companyservice.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);

    boolean existsByName(String name);

}
