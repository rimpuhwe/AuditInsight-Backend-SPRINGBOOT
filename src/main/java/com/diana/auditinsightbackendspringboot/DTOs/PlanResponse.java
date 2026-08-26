package com.diana.auditinsightbackendspringboot.DTOs;

import com.diana.auditinsightbackendspringboot.Enum.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PlanResponse {
    private SubscriptionType code;
    private int durationDays;
    private BigDecimal amount;
    private String currency;
}
