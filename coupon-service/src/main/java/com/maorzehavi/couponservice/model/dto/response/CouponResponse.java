package com.maorzehavi.couponservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CouponResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer amount;

    private Double price;

    private String image;
}
