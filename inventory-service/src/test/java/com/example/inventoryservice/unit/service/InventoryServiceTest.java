package com.example.inventoryservice.unit.service;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InMemoryStockRepository;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        InMemoryStockRepository repository = new InMemoryStockRepository();
        repository.init();
        inventoryService = new InventoryService(repository);
    }

    @Test
    void shouldApproveRequestWhenStockIsAvailable() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(2);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void shouldRejectRequestWhenStockIsUnavailable() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(20);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    void shouldAssignTruckWhenRequestIsApproved() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(1);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertNotNull(response.getTruckId());
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void shouldReturnReasonWhenRequestIsRejected() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(99);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("REJECTED", response.getStatus());
        assertEquals("OUT_OF_STOCK", response.getReason());
    }

    @Test
    void shouldReduceStockWhenRequestIsApproved() {
        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(1);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
        assertEquals(9, response.getRemainingStock());
    }
}