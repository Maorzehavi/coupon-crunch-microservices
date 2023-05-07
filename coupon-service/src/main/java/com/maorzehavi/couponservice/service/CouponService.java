package com.maorzehavi.couponservice.service;

import com.maorzehavi.couponservice.repository.CouponRepository;
import com.maorzehavi.couponservice.service.client.CompanyClient;
import com.maorzehavi.couponservice.service.client.InventoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    @LoadBalanced
    private final InventoryClient inventoryClient;

    public void deleteCoupon(Long id) {
        inventoryClient.deleteCouponById(id);
        couponRepository.deleteById(id);
    }


}
