package com.maorzehavi.couponservice.service;

import com.maorzehavi.couponservice.repository.CouponRepository;
import com.maorzehavi.couponservice.service.client.CompanyClient;
import com.maorzehavi.couponservice.service.client.InventoryClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public String test(HttpServletRequest request) {
        request.getHeader("Authorization")
        System.out.println("CouponService.test");
        return "test";
    }


}
