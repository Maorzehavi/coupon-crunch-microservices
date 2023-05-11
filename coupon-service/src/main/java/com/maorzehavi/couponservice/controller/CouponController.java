package com.maorzehavi.couponservice.controller;

import com.maorzehavi.couponservice.model.dto.request.CouponRequest;
import com.maorzehavi.couponservice.model.dto.response.CouponResponse;
import com.maorzehavi.couponservice.service.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest couponRequest, HttpServletRequest httpServletRequest) {
        return couponService.createCoupon(couponRequest, httpServletRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getCoupon(@PathVariable Long id) {
        return couponService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CouponResponse>> getCouponByCompanyId(@PathVariable Long companyId) {
        return couponService.findAllByCompanyId(companyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CouponResponse>> getCouponByCategoryId(@PathVariable Long categoryId) {
        return couponService.findAllByCategoryId(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> updateCoupon(@PathVariable Long id, @RequestBody CouponRequest couponRequest, HttpServletRequest httpServletRequest) {
        return couponService.updateCoupon(id, couponRequest, httpServletRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        if (couponService.deleteCoupon(id, httpServletRequest)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/exists/{categoryId}")
    public boolean existsForCategory(@PathVariable Long categoryId) {
        return couponService.existsByCategoryId(categoryId);
    }

    @GetMapping("/purchase")
    @Transactional
    public ResponseEntity<?> purchaseCoupons(@RequestParam("coupon") Long... coupons) {
        List<Long> idsList = new ArrayList<>(Arrays.asList(coupons));
        idsList.removeIf(id -> !couponService.buyCoupon(id));
        double totalPrice = couponService.getTotalPrice(idsList);
        return ResponseEntity.ok(idsList+"$"+totalPrice);
    }
}
