package com.maorzehavi.userservice.model.dto.response;

import com.maorzehavi.userservice.model.ClientType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private ClientType clientType;
    private Set<RoleResponse> roles;

}