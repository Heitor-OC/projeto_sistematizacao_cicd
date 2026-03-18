package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InMemoryStockRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InMemoryStockRepository stockRepository;

    public InventoryService(InMemoryStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public InventoryResponse process(InventoryRequest request) {
        int available = stockRepository.getAvailableQuantity(request.getProduct());

        if (request.getQuantity() > available) {
            return new InventoryResponse(
                    "REJECTED",
                    null,
                    "OUT_OF_STOCK",
                    request.getProduct(),
                    request.getQuantity(),
                    available
            );
        }

        int remaining = available - request.getQuantity();
        stockRepository.updateStock(request.getProduct(), remaining);

        return new InventoryResponse(
                "APPROVED",
                assignTruck(request.getProduct(), request.getQuantity()),
                null,
                request.getProduct(),
                request.getQuantity(),
                remaining
        );
    }

    private String assignTruck(String product, Integer quantity) {
        return "TRUCK-" + product.toUpperCase().charAt(0) + "-" + quantity;
    }
}