package com.maorzehavi.categoryservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRequest {
    @NotBlank(message = "Category name cannot be empty")
    private String name;
}
