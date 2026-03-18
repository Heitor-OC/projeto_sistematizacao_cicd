package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    public InventoryResponse process(InventoryRequest request) {
        InventoryResponse response = new InventoryResponse();

        if (request.getQuantity() != null && request.getQuantity() > 10) {
            response.setStatus("REJECTED");
            response.setReason("OUT_OF_STOCK");
            return response;
        }

        response.setStatus("APPROVED");
        response.setTruckId("TRUCK-1");
        return response;
    }
}