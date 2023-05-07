package com.maorzehavi.companyservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Boolean isActive;
}