package com.zosh.zosh.pos.system.service.impl;

import com.zosh.zosh.pos.system.service.CouponService;
import com.zosh.zosh.pos.system.service.OrderService;
import com.zosh.zosh.pos.system.service.RazorpayService;
import com.zosh.zosh.pos.system.service.UserService;
import com.zosh.zosh.pos.system.domain.OrderStatus;
import com.zosh.zosh.pos.system.domain.PaymentType;
import com.zosh.zosh.pos.system.domain.UserRole;
import com.zosh.zosh.pos.system.mapper.OrderMapper;
import com.zosh.zosh.pos.system.modal.Branch;
import com.zosh.zosh.pos.system.modal.Order;
import com.zosh.zosh.pos.system.modal.OrderItem;
import com.zosh.zosh.pos.system.modal.Product;
import com.zosh.zosh.pos.system.modal.User;
import com.zosh.zosh.pos.system.payload.dto.OrderDto;
import com.zosh.zosh.pos.system.repository.BranchRepository;
import com.zosh.zosh.pos.system.repository.OrderItemRepository;
import com.zosh.zosh.pos.system.repository.OrderRepository;
import com.zosh.zosh.pos.system.repository.ProductRepository;
import com.zosh.zosh.pos.system.service.CouponService;
import com.zosh.zosh.pos.system.service.OrderService;
import com.zosh.zosh.pos.system.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BranchRepository branchRepository;
     private final CouponService couponService;
     private final RazorpayService razorpayService;

    @Override
    public OrderDto createOrder(OrderDto orderDto) throws Exception {

        // Get currently logged-in user (cashier, manager, or admin)
        User cashier = userService.getCurrentUser();

        // Resolve branch based on role.
        Branch branch = resolveBranch(cashier, orderDto);

        // Create Order
        Order order = Order.builder()
                .branch(branch)
                .cashier(cashier)
                .customer(orderDto.getCustomer())
                .paymentType(orderDto.getPaymentType())
                .build();

        // Create OrderItems
        List<OrderItem> orderItems = orderDto.getItems()
                .stream()
                .map(itemDto -> {

                    Product product = productRepository
                            .findById(itemDto.getProductId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Product not found with id "
                                                    + itemDto.getProductId()
                                    )
                            );

                    double price =
                            product.getSellingPrice() * itemDto.getQuantity();

                    return OrderItem.builder()
                            .product(product)
                            .quantity(itemDto.getQuantity())
                            .price(price)
                            .order(order)
                            .build();
                })
                .toList();

        // Calculate total amount
        double totalAmount = orderItems
                .stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();

        // Apply coupon/discount if one was provided
        double discountAmount = 0.0;
        String couponCode = orderDto.getCouponCode();

        if (couponCode != null && !couponCode.isBlank()) {
            discountAmount = couponService.validateAndCalculateDiscount(
                    couponCode,
                    branch.getStore().getId(),
                    totalAmount
            );
        }

        double finalAmount = totalAmount - discountAmount;

        // Set order details
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        order.setCouponCode(couponCode != null ? couponCode.trim().toUpperCase() : null);
        order.setItems(orderItems);

        if (orderDto.getPaymentType() == PaymentType.CASH) {
            order.setStatus(OrderStatus.COMPLETED);
        } else {
            // UPI / CARD must go through Razorpay Checkout first. The frontend
            // sends back these three fields once the customer has paid - we
            // re-verify here rather than trusting the frontend's word for it.
            if (orderDto.getRazorpayOrderId() == null
                    || orderDto.getRazorpayPaymentId() == null
                    || orderDto.getRazorpaySignature() == null) {
                throw new Exception(
                        "Razorpay payment details are required for " + orderDto.getPaymentType() + " orders"
                );
            }

            boolean verified = razorpayService.verifySignature(
                    orderDto.getRazorpayOrderId(),
                    orderDto.getRazorpayPaymentId(),
                    orderDto.getRazorpaySignature()
            );

            if (!verified) {
                throw new Exception("Payment verification failed");
            }

            order.setRazorpayOrderId(orderDto.getRazorpayOrderId());
            order.setRazorpayPaymentId(orderDto.getRazorpayPaymentId());
            order.setStatus(OrderStatus.COMPLETED);
        }

        // Save Order (cascades to OrderItems via CascadeType.ALL)
        Order savedOrder = orderRepository.save(order);

        // Mark coupon as used only after the order is safely saved
        if (couponCode != null && !couponCode.isBlank()) {
            couponService.incrementUsage(couponCode, branch.getStore().getId());
        }

        // Convert Entity -> DTO
        return OrderMapper.toDTO(savedOrder);
    }

    /**
     * Resolves the branch an order should be attached to.
     * - ROLE_BRANCH_CASHIER / ROLE_BRANCH_MANAGER: tied to a single branch,
     *   already assigned on their user record.
     * - ROLE_ADMIN / ROLE_STORE_ADMIN / ROLE_STORE_MANAGER: may operate across
     *   multiple branches, so they must supply branchId in the request.
     */
    private Branch resolveBranch(User user, OrderDto orderDto) throws Exception {

        UserRole role = user.getRole();

        if (role == UserRole.ROLE_BRANCH_CASHIER || role == UserRole.ROLE_BRANCH_MANAGER) {
            Branch branch = user.getBranch();
            if (branch == null) {
                throw new Exception("Cashier's branch not found");
            }
            return branch;
        }

        // Store-level roles must specify the branch explicitly
        if (orderDto.getBranchId() == null) {
            throw new Exception("branchId is required for this role");
        }

        return branchRepository.findById(orderDto.getBranchId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Branch not found with id " + orderDto.getBranchId()
                        )
                );
    }


    @Override
    public OrderDto getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found with id " + id
                        )
                );

        return OrderMapper.toDTO(order);
    }


    @Override
    public List<OrderDto> getOrdersByBranch(
            Long branchId,
            Long customerId,
            Long cashierId,
            PaymentType paymentType,
            OrderStatus status
    ) {

        return orderRepository.findByBranchId(branchId)
                .stream()
                .filter(order ->
                        customerId == null ||
                                (order.getCustomer() != null &&
                                        order.getCustomer().getId().equals(customerId))
                )
                .filter(order ->
                        cashierId == null ||
                                (order.getCashier() != null &&
                                        order.getCashier().getId().equals(cashierId))
                )
                .filter(order ->
                        paymentType == null ||
                                order.getPaymentType() == paymentType
                )
                .filter(order ->
                        status == null ||
                                order.getStatus() == status
                )
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> getOrderByCashier(Long cashierId) {

        return orderRepository.findByCashierId(cashierId)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found with id " + id
                        )
                );

        orderRepository.delete(order);
    }


    @Override
    public List<OrderDto> getTodayOrdersByBranch(Long branchId) {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return orderRepository
                .findByBranchIdAndCreatedAtBetween(branchId, start, end)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {

        return orderRepository
                .findByCustomerId(customerId)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> getTop5RecentOrdersByBranchId(Long branchId) {

        return orderRepository
                .findTop5ByBranchIdOrderByCreatedAtDesc(branchId)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
}