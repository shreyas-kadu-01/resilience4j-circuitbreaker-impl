package com.example.userservice.controllers;

import com.example.userservice.dto.OrderDTO;
import com.example.userservice.services.CatalogServices;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static com.example.userservice.constants.CatalogConst.BASE_URL;

@RestController
@RequestMapping("/catalog/v2")
public class CatalogControllerV2 {

    @Autowired
    private CatalogServices catalogServices;

    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .permittedNumberOfCallsInHalfOpenState(3)
            .slidingWindowSize(10)
            .enableAutomaticTransitionFromOpenToHalfOpen()
            .minimumNumberOfCalls(5)
            .recordExceptions(IOException.class, TimeoutException.class)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .build();

    CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

    CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("test");

    @GetMapping("/displayOrders")
    public List<OrderDTO> displayOrders() {
        Supplier<List<OrderDTO>> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, catalogServices::getProductsByCall);
        return Try.ofSupplier(decoratedSupplier).recover(throwable -> catalogServices.getAllAvailableProducts()).get();
    }
}
