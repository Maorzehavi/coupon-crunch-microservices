package com.maorzehavi.purchaseservice.model.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PurchaseRequest {

    private Long customerId;

    private Long[] couponIds;
}
