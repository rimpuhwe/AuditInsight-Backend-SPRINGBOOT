package com.diana.auditinsightbackendspringboot.DTOs;

import com.diana.auditinsightbackendspringboot.Enum.SubscriptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartMomoCheckoutRequest {

    @NotNull(message = "Subscription type is required (MONTHLY, SIX_MONTHS or ANNUAL)")
    private SubscriptionType subscriptionType;

    @NotBlank(message = "Phone number is required for MoMo payments")
    private String phoneNumber;
}
