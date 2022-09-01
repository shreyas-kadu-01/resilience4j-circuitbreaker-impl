package com.example.catalogservice.controllers;

import com.example.catalogservice.entity.Order;
import com.example.catalogservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    public void initOrderTable() {
        orderRepository.saveAll(Stream.of(
                new Order("Mobile", "ELE", "white", 20000),
                new Order("T-Shirt", "Cloth", "black", 500),
                new Order("Jeans", "Cloth", "blue", 1000),
                new Order("Laptop", "ELE", "grey", 70000),
                new Order("Watch", "fation", "blue", 5000),
                new Order("Fan", "ELE", "white", 3000)
        ).collect(Collectors.toList()));
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{category}")
    public List<Order> getByCategory(@PathVariable String category) {
        return orderRepository.findByCategory(category);
    }
}
