package com.maorzehavi.userservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityResponse {

    private Long id;
    private String authority;
}