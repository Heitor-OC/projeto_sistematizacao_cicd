package com.example.orderservice.client;

import com.example.orderservice.dto.InventoryRequest;
import com.example.orderservice.dto.InventoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate;
    private final String inventoryUrl;

    public InventoryClient(@Value("${inventory.service.url}") String inventoryUrl) {
        this.restTemplate = new RestTemplate();
        this.inventoryUrl = inventoryUrl;
    }

    public InventoryResponse processOrder(InventoryRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<InventoryRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<InventoryResponse> response = restTemplate.exchange(
                inventoryUrl + "/inventory",
                HttpMethod.POST,
                entity,
                InventoryResponse.class
        );

        return response.getBody();
    }
}