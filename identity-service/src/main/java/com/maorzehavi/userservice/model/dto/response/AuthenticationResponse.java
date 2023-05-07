package com.maorzehavi.userservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationResponse {

    private String token;
}
