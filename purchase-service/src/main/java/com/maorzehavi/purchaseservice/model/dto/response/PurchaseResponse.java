package com.maorzehavi.purchaseservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PurchaseResponse {

    private Long id;

    private Long customerId;

    private String timestamp;

    private Double totalPrice;

    private Integer numberOfCoupons;

    private List<Long> couponIds;

}
