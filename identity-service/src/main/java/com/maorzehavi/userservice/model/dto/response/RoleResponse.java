package com.maorzehavi.userservice.model.dto.response;

import com.maorzehavi.userservice.model.ClientType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoleResponse {

    private Long id;
    private String role;
    private ClientType clientType;
    private Set<AuthorityResponse> authorities;
}