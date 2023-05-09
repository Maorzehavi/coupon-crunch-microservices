package com.maorzehavi.couponservice.service;

import com.maorzehavi.couponservice.model.dto.request.CouponRequest;
import com.maorzehavi.couponservice.model.dto.response.CouponResponse;
import com.maorzehavi.couponservice.model.entity.Coupon;
import com.maorzehavi.couponservice.repository.CouponRepository;
import com.maorzehavi.couponservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public Optional<CouponResponse> findById(Long id){
        return couponRepository.findById(id)
                .map(this::mapToResponse);
    }

    public Optional<CouponResponse> createCoupon(CouponRequest couponRequest, HttpServletRequest httpServletRequest){
        Long userId = jwtUtil.extractUserId(httpServletRequest);
        Long companyId = restTemplate.getForObject("http://company-service/api/companies/user/" + userId, Long.class);
        if (companyId == null) {
            throw new RuntimeException("Company not found");
        }
        if (existsForCompany(couponRequest.getTitle(), companyId)) {
            throw new RuntimeException("Coupon with title " + couponRequest.getTitle() + " already exists");
        }
        Coupon coupon = mapToEntity(couponRequest);
        coupon.setCompanyId(companyId);
        couponRepository.save(coupon);
        return Optional.of(mapToResponse(coupon));
    }

    public Optional<CouponResponse> updateCoupon(Long id, CouponRequest couponRequest, HttpServletRequest httpServletRequest){
        Long userId = jwtUtil.extractUserId(httpServletRequest);
        Long companyId = restTemplate.getForObject("http://company-service/api/companies/user/" + userId, Long.class);
        if (companyId == null) {
            throw new RuntimeException("Company not found");
        }
        var coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon with id " + id + " not found"));
        if (!coupon.getCompanyId().equals(companyId)) {
            throw new RuntimeException("Coupon with id " + id + " not found");
        }
        if (existsForCompany(couponRequest.getTitle(), companyId)) {
            throw new RuntimeException("Coupon with title " + couponRequest.getTitle() + " already exists");
        }
        coupon.setTitle(couponRequest.getTitle());
        coupon.setDescription(couponRequest.getDescription());
        coupon.setStartDate(LocalDate.parse(couponRequest.getStartDate()));
        coupon.setEndDate(LocalDate.parse(couponRequest.getEndDate()));
        coupon.setAmount(couponRequest.getAmount());
        coupon.setPrice(couponRequest.getPrice());
        coupon.setImage(couponRequest.getImage());
        couponRepository.save(coupon);
        return Optional.of(mapToResponse(coupon));
    }

    public void deleteCoupon(Long id, HttpServletRequest httpServletRequest){
        Long userId = jwtUtil.extractUserId(httpServletRequest);
        Long companyId = restTemplate.getForObject("http://company-service/api/companies/user/" + userId, Long.class);
        if (companyId == null) {
            throw new RuntimeException("Company not found");
        }
        var coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon with id " + id + " not found"));
        if (!coupon.getCompanyId().equals(companyId)) {
            throw new RuntimeException("Coupon with id " + id + " not found");
        }
        couponRepository.deleteById(id);
    }

    public List<CouponResponse> findAllByCompanyId(Long companyId){
        return couponRepository.findAllByCompanyId(companyId)
                .orElseThrow(
                        () -> new RuntimeException("Coupons for company with id " + companyId + " not found")
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CouponResponse> findAllByCategoryId(Long categoryId){
        return couponRepository.findAllByCategoryId(categoryId)
                .orElseThrow(
                        () -> new RuntimeException("Coupons for category with id " + categoryId + " not found")
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private boolean existsForCompany(String couponTitle, Long companyId){
        return couponRepository.existsByTitleAndCompanyId(couponTitle, companyId);
    }


    private CouponResponse mapToResponse(Coupon coupon){
        return CouponResponse.builder()
                .id(coupon.getId())
                .title(coupon.getTitle())
                .description(coupon.getDescription())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .amount(coupon.getAmount())
                .price(coupon.getPrice())
                .image(coupon.getImage())
                .build();
    }

    private Coupon mapToEntity(CouponRequest request){
        return Coupon.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(LocalDate.parse(request.getStartDate()))
                .endDate(LocalDate.parse(request.getEndDate()))
                .amount(request.getAmount())
                .price(request.getPrice())
                .image(request.getImage())
                .build();
    }




}
