package com.diana.auditinsightbackendspringboot.Services;

import com.diana.auditinsightbackendspringboot.Enum.SubscriptionType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PlanService {

    /** The 3 fixed-price subscription periods — backend-controlled, not stored in the database. */
    public Flux<SubscriptionType> listPlans() {
        return Flux.just(SubscriptionType.values());
    }
}
