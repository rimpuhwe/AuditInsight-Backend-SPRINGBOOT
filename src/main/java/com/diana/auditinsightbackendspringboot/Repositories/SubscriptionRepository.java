package com.diana.auditinsightbackendspringboot.Repositories;

import com.diana.auditinsightbackendspringboot.Enum.SubscriptionStatus;
import com.diana.auditinsightbackendspringboot.Models.Subscription;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface SubscriptionRepository extends ReactiveCrudRepository<Subscription, UUID> {

    Mono<Subscription> findByOrganisationIdAndStatus(UUID organisationId, SubscriptionStatus status);

    /** The org's current subscription record regardless of status — used for access gating. */
    Mono<Subscription> findFirstByOrganisationIdOrderByCreatedAtDesc(UUID organisationId);

    /** Housekeeping: subscriptions whose period has lapsed but are still marked TRIAL/ACTIVE. */
    Flux<Subscription> findByStatusInAndEndDateBefore(Collection<SubscriptionStatus> statuses, LocalDateTime cutoff);

    /** 7-day-out expiry reminder window. */
    Flux<Subscription> findByStatusInAndEndDateBetween(
            Collection<SubscriptionStatus> statuses, LocalDateTime windowStart, LocalDateTime windowEnd);
}
