package com.example.userservice.controllers;

import com.example.userservice.dto.OrderDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    public static final String USER_SERVICE = "userService";
    private static final String BASE_URL = "http://localhost:9191/orders/";
    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @GetMapping("/displayOrders")
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getAllAvailableProducts")
    public List<OrderDTO> displayOrders(@RequestParam("category") String category) {
        String url = (category == null) ? BASE_URL : BASE_URL + "/" + category;
        return restTemplate.getForObject(url, ArrayList.class);
    }

    public List<OrderDTO> getAllAvailableProducts(Exception e) {
        return Stream.of(
                        new OrderDTO(119, "LED TV", "ELE", "white", 45000),
                        new OrderDTO(120, "LCD TV", "ELE", "black", 45000),
                        new OrderDTO(1122, "Light", "ELE", "blue", 50),
                        new OrderDTO(113, "T-Shirt", "Cloths", "white", 500),
                        new OrderDTO(13, "Jeans", "Cloths", "blue denim", 1700))
                .collect(Collectors.toList());
    }
}
