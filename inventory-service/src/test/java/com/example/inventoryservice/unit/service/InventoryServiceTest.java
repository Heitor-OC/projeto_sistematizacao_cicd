package com.example.inventoryservice.unit.service;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
//import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.repository.ProductRepository;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private InventoryService inventoryService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        inventoryService = new InventoryService(productRepository);
    }

    @Test
    void shouldApproveRequestWhenStockIsAvailable() {
        Product product = new Product();
        product.setName("Notebook");
        product.setQuantity(10);

        when(productRepository.findByName("Notebook"))
                .thenReturn(Optional.of(product));

        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(2);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void shouldRejectRequestWhenStockIsUnavailable() {
        Product product = new Product();
        product.setName("Notebook");
        product.setQuantity(10);

        when(productRepository.findByName("Notebook"))
                .thenReturn(Optional.of(product));

        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(20);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    void shouldAssignTruckWhenRequestIsApproved() {
        Product product = new Product();
        product.setName("Notebook");
        product.setQuantity(10);

        when(productRepository.findByName("Notebook"))
                .thenReturn(Optional.of(product));

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
        Product product = new Product();
        product.setName("Notebook");
        product.setQuantity(10);

        when(productRepository.findByName("Notebook"))
                .thenReturn(Optional.of(product));

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
        Product product = new Product();
        product.setName("Notebook");
        product.setQuantity(10);

        when(productRepository.findByName("Notebook"))
                .thenReturn(Optional.of(product));

        InventoryRequest request = new InventoryRequest();
        request.setProduct("Notebook");
        request.setQuantity(1);

        InventoryResponse response = inventoryService.process(request);

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
        assertEquals(9, response.getRemainingStock());

        verify(productRepository).save(product); // garante persistência
    }
}