package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public InventoryResponse process(InventoryRequest request) {

        Product product = productRepository.findByName(request.getProduct())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProduct()));

        int available = product.getQuantity();

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

        // Atualiza no banco
        product.setQuantity(remaining);
        productRepository.save(product);

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