package com.maorzehavi.purchaseservice.service;

import com.maorzehavi.purchaseservice.model.dto.request.PurchaseRequest;
import com.maorzehavi.purchaseservice.model.dto.response.PurchaseResponse;
import com.maorzehavi.purchaseservice.model.entity.Purchase;
import com.maorzehavi.purchaseservice.repository.PurchaseRepository;
import com.maorzehavi.purchaseservice.util.CouponPurchaseResponseParser;
import com.maorzehavi.purchaseservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerInventoryService customerInventoryService;
    private final SequenceGenerator sequenceGenerator;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public Optional<PurchaseResponse> createPurchase(PurchaseRequest purchaseRequest, HttpServletRequest httpRequest) {
        long userId = jwtUtil.extractUserId(httpRequest);
        Long customerId = restTemplate.getForObject("http://customer-service/api/v1/customers/user/" + userId, Long.class);

        Purchase purchase = Purchase.builder().customerId(customerId).build();
        String request = "http://coupon-service/api/v1/coupons/purchase?coupon=" + Arrays.toString(purchaseRequest.getCouponIds());
        request = request.replace("[", "").replace("]", "").replace(" ", "");
        System.out.println(request);
        var restResponse = restTemplate.postForObject(request, null, String.class);
        assert restResponse != null;
        var response = CouponPurchaseResponseParser.parseResponse(restResponse);
        purchase.setTotalPrice(response.getDoubleValue());
        purchase.setNumberOfCoupons(response.getLongList().size());
        purchase.setId(sequenceGenerator.generateNextId(Purchase.SEQUENCE_NAME));
        purchase.setCouponIds(response.getLongList());
        purchase.setTimestamp(new Timestamp(System.currentTimeMillis()));
        purchase = purchaseRepository.save(purchase);
        customerInventoryService.saveCustomerInventory(customerId, purchase.getCouponIds().toArray(Long[]::new));
        return Optional.of(mapToResponse(purchase));
    }


    public Optional<PurchaseResponse> findById(Long id) {
        return purchaseRepository.findById(id)
                .map(this::mapToResponse);
    }

    public Optional<List<PurchaseResponse>> findAllByCustomerId(Long customerId) {
        return Optional.of(purchaseRepository.findAllByCustomerIdOrderByTimestamp(customerId).orElseThrow(
                () -> new RuntimeException("No purchases found for customer id: " + customerId)
        ).stream().map(this::mapToResponse).toList());
    }

    public List<PurchaseResponse> findAll() {
        return purchaseRepository.findAll().stream().map(this::mapToResponse).toList();
    }


    private PurchaseResponse mapToResponse(Purchase purchase) {
        return PurchaseResponse.builder()
                .id(purchase.getId())
                .customerId(purchase.getCustomerId())
                .timestamp(purchase.getTimestamp().toString())
                .totalPrice(purchase.getTotalPrice())
                .numberOfCoupons(purchase.getNumberOfCoupons())
                .couponIds(purchase.getCouponIds())
                .build();
    }


    public void deleteById(Long id) {
        purchaseRepository.deleteById(id);
    }
}
