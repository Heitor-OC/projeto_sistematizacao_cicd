package com.example.orderservice.unit.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void shouldCallInventoryClientWhenCreatingOrder() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Notebook");
        request.setQuantity(2);

        when(inventoryClient.processOrder(request))
                .thenReturn(new InventoryResponse());

        orderService.createOrder(request);

        verify(inventoryClient, times(1)).processOrder(request);
    }

    @Test
    void shouldNotThrowExceptionWhenInventoryApprovesOrder() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Notebook");
        request.setQuantity(2);

        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");

        when(inventoryClient.processOrder(request)).thenReturn(response);

        orderService.createOrder(request);

        verify(inventoryClient).processOrder(request);
    }

    @Test
    void shouldThrowExceptionWhenInventoryRejectsOrder() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Teste de produto");
        request.setQuantity(20);

        InventoryResponse response = new InventoryResponse();
        response.setStatus("REJECTED");
        response.setReason("OUT_OF_STOCK");

        when(inventoryClient.processOrder(request)).thenReturn(response);

        assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
    }

    @Test
    void shouldHandleDifferentProducts() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Teste de produto");
        request.setQuantity(1);

        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");

        when(inventoryClient.processOrder(request)).thenReturn(response);

        orderService.createOrder(request);

        verify(inventoryClient).processOrder(request);
    }

    @Test
    void shouldPassRequestObjectToInventoryClient() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Teste de produto");
        request.setQuantity(3);

        when(inventoryClient.processOrder(request))
                .thenReturn(new InventoryResponse());

        orderService.createOrder(request);

        verify(inventoryClient).processOrder(request);
    }
}