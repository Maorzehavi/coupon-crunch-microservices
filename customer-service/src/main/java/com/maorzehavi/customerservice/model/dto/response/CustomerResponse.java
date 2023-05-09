package com.maorzehavi.customerservice.model.dto.response;

import com.maorzehavi.customerservice.model.dto.request.UserRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Long userId;

}