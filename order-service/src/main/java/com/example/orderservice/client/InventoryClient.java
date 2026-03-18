package com.example.orderservice.client;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderRequest;
import org.springframework.stereotype.Component;

@Component
public class InventoryClient {

    public InventoryResponse processOrder(OrderRequest request) {
        throw new UnsupportedOperationException("Ainda nao foi implementada");
    }
}