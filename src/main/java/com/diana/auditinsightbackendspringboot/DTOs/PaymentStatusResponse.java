package com.diana.auditinsightbackendspringboot.DTOs;

import com.diana.auditinsightbackendspringboot.Enum.PaymentProvider;
import com.diana.auditinsightbackendspringboot.Enum.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentStatusResponse {
    private UUID paymentId;
    private PaymentProvider provider;
    private PaymentStatus status;
    private BigDecimal expectedAmount;
    private BigDecimal receivedAmount;
    private String currency;
    private UUID subscriptionId;
    private String failureReason;
}
