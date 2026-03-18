package com.example.inventoryservice.unit.service;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private final InventoryService inventoryService = new InventoryService();

    @Test
    void shouldApproveRequestWhenStockIsAvailable() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Testando produto");
        request.setQuantity(2);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void shouldRejectRequestWhenStockIsUnavailable() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Testando produto");
        request.setQuantity(20);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    void shouldAssignTruckWhenRequestIsApproved() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Testando produto");
        request.setQuantity(1);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertNotNull(response.getTruckId());
    }

    @Test
    void shouldReturnReasonWhenRequestIsRejected() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Testando produto");
        request.setQuantity(99);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertNotNull(response.getReason());
    }

    @Test
    void shouldHandleKnownProducts() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Testando produto");
        request.setQuantity(1);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
    }
}