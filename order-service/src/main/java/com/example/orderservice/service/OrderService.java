package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final InventoryClient inventoryClient;

    public OrderService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    public void createOrder(OrderRequest request) {
        InventoryResponse response = inventoryClient.processOrder(request);

        if (response != null && "REJECTED".equals(response.getStatus())) {
            throw new RuntimeException("Pedido rejeitado");
        }
    }
}