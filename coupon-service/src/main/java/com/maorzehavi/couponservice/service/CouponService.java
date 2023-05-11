package com.maorzehavi.couponservice.service;

import com.maorzehavi.couponservice.model.dto.request.CouponRequest;
import com.maorzehavi.couponservice.model.dto.response.CouponResponse;
import com.maorzehavi.couponservice.model.entity.Coupon;
import com.maorzehavi.couponservice.repository.CouponRepository;
import com.maorzehavi.couponservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public Optional<CouponResponse> findById(Long id) {
        return couponRepository.findById(id)
                .map(this::mapToResponse);
    }

    public Optional<CouponResponse> createCoupon(CouponRequest couponRequest, HttpServletRequest httpServletRequest) {
        Long userId = jwtUtil.extractUserId(httpServletRequest);
        Long companyId = restTemplate.getForObject("http://company-service/api/v1/companies/user/" + userId, Long.class);
        if (companyId == null) {
            throw new RuntimeException("Company not found");
        }
        if (getIdByTitleAndCompanyId(couponRequest.getTitle(), companyId) != -1L) {
            throw new RuntimeException("Coupon with title " + couponRequest.getTitle() + " already exists for company with id " + companyId);
        }
        boolean categoryExists = Boolean.TRUE.equals(restTemplate.getForObject("http://category-service/api/v1/categories/exists/" + couponRequest.getCategoryId(), Boolean.class));
        if (!categoryExists) {
            throw new RuntimeException("Category with id " + couponRequest.getCategoryId() + " not found");
        }
        Coupon coupon = mapToEntity(couponRequest);
        coupon.setCompanyId(companyId);
        couponRepository.save(coupon);
        return Optional.of(mapToResponse(coupon));
    }

    public Optional<CouponResponse> updateCoupon(Long id, CouponRequest couponRequest, HttpServletRequest httpServletRequest) {
        Long userId = jwtUtil.extractUserId(httpServletRequest);
        Long companyId = restTemplate.getForObject("http://company-service/api/v1/companies/user/" + userId, Long.class);
        if (companyId == null) {
            throw new RuntimeException("Company not found");
        }
        var coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon with id " + id + " not found"));
        if (!coupon.getCompanyId().equals(companyId)) {
            throw new RuntimeException("Coupon with id " + id + " not found");
        }
        if (!Objects.equals(getIdByTitleAndCompanyId(couponRequest.getTitle(), companyId), coupon.getId())) {
            throw new RuntimeException("Coupon with title " + couponRequest.getTitle() + " already exists for company with id " + companyId);
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

    public boolean deleteCoupon(Long id, HttpServletRequest httpServletRequest) {
        try {
            Long userId = jwtUtil.extractUserId(httpServletRequest);
            Long companyId = restTemplate.getForObject("http://company-service/api/v1/companies/user/" + userId, Long.class);
            if (companyId == null) {
                throw new RuntimeException("Company not found");
            }
            var coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon with id " + id + " not found"));
            if (!coupon.getCompanyId().equals(companyId)) {
                throw new RuntimeException("Coupon with id " + id + " not found");
            }
            couponRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Optional<List<CouponResponse>> findAllByCompanyId(Long companyId) {
        return couponRepository.findAllByCompanyId(companyId)
                .map(coupons -> coupons.stream()
                        .map(this::mapToResponse)
                        .toList());
    }

    public Optional<List<CouponResponse>> findAllByCategoryId(Long categoryId) {
        return couponRepository.findAllByCategoryId(categoryId)
                .map(coupons -> coupons.stream()
                        .map(this::mapToResponse)
                        .toList());
    }

    public List<CouponResponse> findAll() {
        return couponRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CouponResponse> findAllIfExist(Long... ids) {
        List<Coupon> coupons = couponRepository.findAllById(List.of(ids));
        return coupons.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Modifying
    @Transactional
    public void changeAmount(Long couponId, Integer amount) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RuntimeException("Coupon with id " + couponId + " not found"));
        coupon.setAmount(coupon.getAmount() + amount);
        couponRepository.save(coupon);
    }

    @Modifying
    @Transactional
    public boolean buyCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null || coupon.getAmount() == 0) {
            return false;
        }
        coupon.setAmount(coupon.getAmount() - 1);
        couponRepository.save(coupon);
        return true;
    }

    public boolean existsByCategoryId(Long categoryId) {
        return couponRepository.existsByCategoryId(categoryId);
    }

    private Long getIdByTitleAndCompanyId(String couponTitle, Long companyId) {
        if (couponRepository.getIdByTitleAndCompanyId(couponTitle, companyId).isPresent()) {
            return couponRepository.getIdByTitleAndCompanyId(couponTitle, companyId).get();
        }
        return -1L;
    }


    private CouponResponse mapToResponse(Coupon coupon) {
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

    private Coupon mapToEntity(CouponRequest request) {
        return Coupon.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(LocalDate.parse(request.getStartDate()))
                .endDate(LocalDate.parse(request.getEndDate()))
                .amount(request.getAmount())
                .price(request.getPrice())
                .image(request.getImage())
                .categoryId(request.getCategoryId())
                .build();
    }


    public double getTotalPrice(List<Long> idsList) {
        double totalPrice = 0;
        for (Long id : idsList) {
            Coupon coupon = couponRepository.findById(id).orElse(null);
            if(coupon != null){
                totalPrice += coupon.getPrice();
            }
        }
        return totalPrice;
    }
}
