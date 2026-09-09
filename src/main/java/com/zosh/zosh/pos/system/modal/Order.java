package com.zosh.zosh.pos.system.modal;

import com.zosh.zosh.pos.system.domain.OrderStatus;
import com.zosh.zosh.pos.system.domain.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;

    private Double totalAmount;

    private LocalDateTime createdAt;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private User cashier;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private PaymentType paymentType;

    private String couponCode;

    private Double discountAmount;

    private Double finalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String razorpayOrderId;

    private String razorpayPaymentId;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

    }



}
