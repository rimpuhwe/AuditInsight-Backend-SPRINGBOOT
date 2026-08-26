package com.diana.auditinsightbackendspringboot.DTOs;

import com.diana.auditinsightbackendspringboot.Enum.SubscriptionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartCardCheckoutRequest {

    @NotNull(message = "Subscription type is required (MONTHLY, SIX_MONTHS or ANNUAL)")
    private SubscriptionType subscriptionType;
}
