package com.maorzehavi.userservice.model.dto.request;

import com.maorzehavi.userservice.model.ClientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoleRequest {
    @NotBlank(message = "role is mandatory")
    private String role;
    @NotNull
    private ClientType clientType;
    @NotNull
    private Set<AuthorityRequest> authorities;
}