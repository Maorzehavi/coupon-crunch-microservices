package com.maorzehavi.userservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityRequest {
    @NotBlank(message = "Authority is mandatory")
    private String authority;
}