package com.maorzehavi.couponservice.controller;

import com.maorzehavi.couponservice.model.dto.request.CouponRequest;
import com.maorzehavi.couponservice.model.dto.response.CouponResponse;
import com.maorzehavi.couponservice.service.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest couponRequest, HttpServletRequest httpServletRequest){
        return couponService.createCoupon(couponRequest, httpServletRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/category/exists/{categoryId}")
    public boolean existsForCategory(@PathVariable Long categoryId){
        return couponService.existsByCategoryId(categoryId);
    }
}
