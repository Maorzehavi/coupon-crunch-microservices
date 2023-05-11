package com.maorzehavi.purchaseservice.service;

import com.maorzehavi.purchaseservice.model.dto.request.PurchaseRequest;
import com.maorzehavi.purchaseservice.model.entity.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final CustomerInventoryService customerInventoryService;
    private final SequenceGenerator sequenceGenerator;


    private Purchase mapToEntity(PurchaseRequest purchaseRequest) {
        return Purchase.builder()
                .customerId(purchaseRequest.getCustomerId())
                .numberOfCoupons(purchaseRequest.getCouponIds().size())
                .build();
    }
}
