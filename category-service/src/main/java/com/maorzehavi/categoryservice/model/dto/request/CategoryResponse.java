package com.maorzehavi.categoryservice.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
}