package com.maorzehavi.couponservice.service.client;

import com.maorzehavi.couponservice.model.dto.response.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service")
public interface CompanyClient {

    @GetMapping("/api/v1/companies/{id}")
    CompanyResponse getCompanyById(@PathVariable Long id);
}
