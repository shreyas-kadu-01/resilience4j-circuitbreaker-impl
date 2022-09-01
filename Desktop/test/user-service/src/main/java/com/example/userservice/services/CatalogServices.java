package com.example.userservice.services;

import com.example.userservice.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.userservice.constants.CatalogConst.BASE_URL;

@Service
public class CatalogServices {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    public List<OrderDTO> getProductsByCall() {
        return restTemplate.getForObject(BASE_URL, ArrayList.class);
    }

    public List<OrderDTO> getAllAvailableProducts() {
        return Stream.of(
                        new OrderDTO(119, "LED TV", "ELE", "white", 45000),
                        new OrderDTO(120, "LCD TV", "ELE", "black", 45000),
                        new OrderDTO(1122, "Light", "ELE", "blue", 50),
                        new OrderDTO(113, "T-Shirt", "Cloths", "white", 500),
                        new OrderDTO(13, "Jeans", "Cloths", "blue denim", 1700))
                .collect(Collectors.toList());
    }
}
