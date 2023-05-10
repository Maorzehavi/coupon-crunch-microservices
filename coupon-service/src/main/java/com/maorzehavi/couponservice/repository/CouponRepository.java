package com.maorzehavi.couponservice.repository;

import com.maorzehavi.couponservice.model.entity.Coupon;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Transactional
    Optional<List<Coupon>> findAllByCategoryId(Long categoryId);

    @Query("select c from Coupon c where c.companyId = ?1")
    @Transactional
    Optional<List<Coupon>> findAllByCompanyId(Long companyId);

    @Modifying
    @Transactional
    @Query("update Coupon c set c.amount = c.amount + :amount where c.id = :id")
    void updateCouponAmount(Long id, Integer amount);

    Optional<List<Coupon>> findAllByTitle(String title);

    @Query("select c from Coupon c where c.price <= :maxPrice")
    Optional<List<Coupon>> findAllByPriceLessThan(Double maxPrice);

    @Query("select c.id from Coupon c where c.title = :title and c.companyId = :companyId")
    Optional<Long> getIdByTitleAndCompanyId(String title, Long companyId);

    @Transactional
    Boolean existsByTitleAndCompanyId(String title, Long companyId);

    Boolean existsByCategoryId(Long categoryId);

    @Query("select c from Coupon c where c.endDate < ?1")
    Optional<List<Coupon>> findAllByEndDateBefore(LocalDate localDate);
}
