package com.example.orderservice.unit.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.InventoryRequest;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.exception.OrderRejectedException;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void shouldCallInventoryClientWhenCreatingOrder() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Notebook");
        request.setQuantity(2);

        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");
        response.setProduct("Notebook");
        response.setRequestedQuantity(2);
        response.setTruckId("TRUCK-N-2");
        response.setRemainingStock(8);

        when(inventoryClient.processOrder(any(InventoryRequest.class)))
                .thenReturn(response);

        orderService.createOrder(request);

        verify(inventoryClient, times(1)).processOrder(any(InventoryRequest.class));
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
        response.setProduct("Notebook");
        response.setRequestedQuantity(2);
        response.setTruckId("TRUCK-N-2");
        response.setRemainingStock(8);

        when(inventoryClient.processOrder(any(InventoryRequest.class))).thenReturn(response);

        orderService.createOrder(request);

        verify(inventoryClient).processOrder(any(InventoryRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenInventoryRejectsOrder() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Notebook");
        request.setQuantity(20);

        InventoryResponse response = new InventoryResponse();
        response.setStatus("REJECTED");
        response.setReason("OUT_OF_STOCK");

        when(inventoryClient.processOrder(any(InventoryRequest.class))).thenReturn(response);

        assertThrows(OrderRejectedException.class, () -> orderService.createOrder(request));
    }

    @Test
    void shouldHandleDifferentProducts() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Mouse");
        request.setQuantity(1);

        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");
        response.setProduct("Mouse");
        response.setRequestedQuantity(1);
        response.setTruckId("TRUCK-M-1");
        response.setRemainingStock(29);

        when(inventoryClient.processOrder(any(InventoryRequest.class))).thenReturn(response);

        orderService.createOrder(request);

        verify(inventoryClient).processOrder(any(InventoryRequest.class));
    }

    @Test
    void shouldPassMappedRequestToInventoryClient() {
        InventoryClient inventoryClient = mock(InventoryClient.class);
        OrderService orderService = new OrderService(inventoryClient);

        OrderRequest request = new OrderRequest();
        request.setProduct("Keyboard");
        request.setQuantity(3);

        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");
        response.setProduct("Keyboard");
        response.setRequestedQuantity(3);
        response.setTruckId("TRUCK-K-3");
        response.setRemainingStock(17);

        when(inventoryClient.processOrder(any(InventoryRequest.class)))
                .thenReturn(response);

        orderService.createOrder(request);

        verify(inventoryClient).processOrder(argThat(inventoryRequest ->
                inventoryRequest.getProduct().equals("Keyboard") &&
                        inventoryRequest.getQuantity().equals(3)
        ));
    }
}