package com.zosh.zosh.pos.system.repository;

import com.zosh.zosh.pos.system.modal.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCodeIgnoreCase(String code);

    List<Coupon> findByStoreId(Long storeId);

    Optional<Coupon> findByCodeIgnoreCaseAndStoreId(String code, Long storeId);
}