package com.zosh.zosh.pos.system.service.impl;

import com.zosh.zosh.pos.system.domain.DiscountType;
import com.zosh.zosh.pos.system.mapper.CouponMapper;
import com.zosh.zosh.pos.system.modal.Coupon;
import com.zosh.zosh.pos.system.modal.Store;
import com.zosh.zosh.pos.system.payload.dto.CouponDto;
import com.zosh.zosh.pos.system.payload.dto.CouponUsageDto;
import com.zosh.zosh.pos.system.repository.CouponRepository;
import com.zosh.zosh.pos.system.repository.OrderRepository;
import com.zosh.zosh.pos.system.repository.StoreRepository;
import com.zosh.zosh.pos.system.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.zosh.zosh.pos.system.modal.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    @Override
    public CouponDto createCoupon(CouponDto couponDto) throws Exception {

        couponRepository.findByCodeIgnoreCase(couponDto.getCode())
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "Coupon code '" + couponDto.getCode() + "' already exists"
                    );
                });

        Store store = null;
        if (couponDto.getStoreId() != null) {
            store = storeRepository.findById(couponDto.getStoreId())
                    .orElseThrow(() -> new Exception(
                            "Store not found with id " + couponDto.getStoreId()
                    ));
        }

        Coupon coupon = Coupon.builder()
                .code(couponDto.getCode().trim().toUpperCase())
                .discountType(couponDto.getDiscountType())
                .discountValue(couponDto.getDiscountValue())
                .minOrderAmount(couponDto.getMinOrderAmount())
                .maxDiscountAmount(couponDto.getMaxDiscountAmount())
                .usageLimit(couponDto.getUsageLimit())
                .usedCount(0)
                .expiryDate(couponDto.getExpiryDate())
                .active(couponDto.getActive() == null ? true : couponDto.getActive())
                .store(store)
                .build();

        Coupon saved = couponRepository.save(coupon);

        return CouponMapper.toDTO(saved);
    }

    @Override
    public CouponDto updateCoupon(Long id, CouponDto couponDto) throws Exception {

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Coupon not found with id " + id
                ));

        if (couponDto.getCode() != null) {
            coupon.setCode(couponDto.getCode().trim().toUpperCase());
        }
        if (couponDto.getDiscountType() != null) {
            coupon.setDiscountType(couponDto.getDiscountType());
        }
        if (couponDto.getDiscountValue() != null) {
            coupon.setDiscountValue(couponDto.getDiscountValue());
        }
        if (couponDto.getMinOrderAmount() != null) {
            coupon.setMinOrderAmount(couponDto.getMinOrderAmount());
        }
        if (couponDto.getMaxDiscountAmount() != null) {
            coupon.setMaxDiscountAmount(couponDto.getMaxDiscountAmount());
        }
        if (couponDto.getUsageLimit() != null) {
            coupon.setUsageLimit(couponDto.getUsageLimit());
        }
        if (couponDto.getExpiryDate() != null) {
            coupon.setExpiryDate(couponDto.getExpiryDate());
        }
        if (couponDto.getActive() != null) {
            coupon.setActive(couponDto.getActive());
        }

        Coupon updated = couponRepository.save(coupon);

        return CouponMapper.toDTO(updated);
    }

    @Override
    public void deleteCoupon(Long id) throws Exception {

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Coupon not found with id " + id
                ));

        couponRepository.delete(coupon);
    }

    @Override
    public CouponDto getCouponById(Long id) throws Exception {

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Coupon not found with id " + id
                ));

        return CouponMapper.toDTO(coupon);
    }

    @Override
    public List<CouponDto> getCouponsByStore(Long storeId) {

        return couponRepository.findByStoreId(storeId)
                .stream()
                .map(CouponMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double validateAndCalculateDiscount(
            String code, Long storeId, double orderAmount
    ) throws Exception {

        Coupon coupon = couponRepository
                .findByCodeIgnoreCaseAndStoreId(code, storeId)
                .orElseThrow(() -> new Exception("Invalid coupon code: " + code));

        if (!Boolean.TRUE.equals(coupon.getActive())) {
            throw new Exception("This coupon is no longer active");
        }

        if (coupon.getExpiryDate() != null &&
                coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new Exception("This coupon has expired");
        }

        if (coupon.getUsageLimit() != null &&
                coupon.getUsedCount() != null &&
                coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new Exception("This coupon has reached its usage limit");
        }

        if (coupon.getMinOrderAmount() != null &&
                orderAmount < coupon.getMinOrderAmount()) {
            throw new Exception(
                    "Minimum order amount for this coupon is " + coupon.getMinOrderAmount()
            );
        }

        double discount;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = orderAmount * (coupon.getDiscountValue() / 100.0);

            if (coupon.getMaxDiscountAmount() != null &&
                    discount > coupon.getMaxDiscountAmount()) {
                discount = coupon.getMaxDiscountAmount();
            }
        } else {
            discount = coupon.getDiscountValue();
        }

        if (discount > orderAmount) {
            discount = orderAmount;
        }

        return discount;
    }

    @Override
    public Coupon incrementUsage(String code, Long storeId) throws Exception {

        Coupon coupon = couponRepository
                .findByCodeIgnoreCaseAndStoreId(code, storeId)
                .orElseThrow(() -> new Exception("Invalid coupon code: " + code));

        coupon.setUsedCount(
                (coupon.getUsedCount() == null ? 0 : coupon.getUsedCount()) + 1
        );

        return couponRepository.save(coupon);
    }
    @Override
    public List<CouponUsageDto> getCouponUsageReport(Long storeId) {

        return couponRepository.findByStoreId(storeId)
                .stream()
                .map(coupon -> {
                    List<Order> orders = orderRepository
                            .findByCouponCodeIgnoreCaseAndBranch_Store_Id(coupon.getCode(), storeId);

                    double totalDiscount = orders.stream()
                            .mapToDouble(o -> o.getDiscountAmount() == null ? 0 : o.getDiscountAmount())
                            .sum();

                    double totalRevenue = orders.stream()
                            .mapToDouble(o -> {
                                if (o.getFinalAmount() != null) return o.getFinalAmount();
                                return o.getTotalAmount() == null ? 0 : o.getTotalAmount();
                            })
                            .sum();

                    return CouponUsageDto.builder()
                            .id(coupon.getId())
                            .code(coupon.getCode())
                            .discountType(coupon.getDiscountType())
                            .discountValue(coupon.getDiscountValue())
                            .active(coupon.getActive())
                            .usageLimit(coupon.getUsageLimit())
                            .usedCount(coupon.getUsedCount())
                            .totalOrders(orders.size())
                            .totalDiscountGiven(totalDiscount)
                            .totalRevenue(totalRevenue)
                            .build();
                })
                .collect(Collectors.toList());
    }
}