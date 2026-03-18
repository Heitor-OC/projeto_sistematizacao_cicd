package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.InventoryRequest;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.exception.OrderRejectedException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final InventoryClient inventoryClient;

    public OrderService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    public OrderResponse createOrder(OrderRequest request) {
        InventoryRequest inventoryRequest = new InventoryRequest(request.getProduct(), request.getQuantity()
        );

        InventoryResponse inventoryResponse = inventoryClient.processOrder(inventoryRequest);

        if (inventoryResponse == null) {
            throw new RuntimeException("O servico de estoque retornou uma resposta vazia");
        }

        if ("REJECTED".equalsIgnoreCase(inventoryResponse.getStatus())) {
            throw new OrderRejectedException(
                    inventoryResponse.getReason() != null
                            ? inventoryResponse.getReason()
                            : "Pedido rejeitado pelo estoque"
            );
        }

        return new OrderResponse(
                "Pedido realizado com sucesso",
                inventoryResponse.getProduct(),
                inventoryResponse.getRequestedQuantity(),
                inventoryResponse.getStatus(),
                inventoryResponse.getTruckId(),
                inventoryResponse.getRemainingStock()
        );
    }
}