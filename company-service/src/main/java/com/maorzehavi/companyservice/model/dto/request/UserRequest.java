package com.maorzehavi.companyservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {

    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Size(min = 4, max = 12, message = "password must be between 4 and 12 characters")
    private String password;

}