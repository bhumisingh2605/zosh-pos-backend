package com.zosh.zosh.pos.system.modal;

import com.zosh.zosh.pos.system.domain.PaymentType;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PaymentSummary {

    private PaymentType type;
    private Double totalAmount;
    private int transactionCount;
    private Double percentage;

}
