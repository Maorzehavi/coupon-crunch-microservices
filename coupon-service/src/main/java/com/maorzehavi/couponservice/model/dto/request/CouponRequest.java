package com.maorzehavi.couponservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
@Data
@Builder
public class CouponRequest {
    @NotBlank(message = "Title is mandatory")
    @Size(min = 2, max = 20, message = "Title must be between 2 and 20 characters")
    private String title;
    @NotBlank(message = "Description is mandatory")
    @Size(min = 2, max = 200, message = "Description must be between 2 and 200 characters")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Start date is mandatory")
    private String startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "End date is mandatory")
    private String endDate;

    @NotNull(message = "Amount is mandatory")
    private Integer amount;

    @NotNull(message = "Price is mandatory")
    private Double price;

    private String image;

    @NotNull(message = "Category id is mandatory")
    private Long categoryId;
}
