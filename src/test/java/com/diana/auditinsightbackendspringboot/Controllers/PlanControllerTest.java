package com.diana.auditinsightbackendspringboot.Controllers;

import com.diana.auditinsightbackendspringboot.Enum.SubscriptionType;
import com.diana.auditinsightbackendspringboot.Exceptions.Global.GlobalExceptionHandler;
import com.diana.auditinsightbackendspringboot.Services.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanControllerTest {

    @Mock
    private PlanService planService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToController(new PlanController(planService))
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void list_returnsAllThreeFixedPriceSubscriptionPeriods() {
        when(planService.listPlans()).thenReturn(Flux.just(SubscriptionType.values()));

        webTestClient.get().uri("/api/plans")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class)
                .hasSize(3);
    }
}
