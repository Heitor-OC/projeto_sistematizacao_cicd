package com.example.orderservice.unit.controller;

import com.example.orderservice.controller.OrderController;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.exception.OrderRejectedException;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Test
    void shouldReturnOkWhenRequestIsValid() {
        OrderService orderService = mock(OrderService.class);
        OrderController controller = new OrderController(orderService);

        OrderRequest request = new OrderRequest();
        request.setProduct("Notebook");
        request.setQuantity(2);

        OrderResponse serviceResponse = new OrderResponse(
                "Order created successfully",
                "Notebook",
                2,
                "APPROVED",
                "TRUCK-N-2",
                8
        );

        when(orderService.createOrder(request)).thenReturn(serviceResponse);

        ResponseEntity<OrderResponse> response = controller.createOrder(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Order created successfully", response.getBody().getMessage());
    }

    @Test
    void shouldCallServiceWhenRequestIsValid() {
        OrderService orderService = mock(OrderService.class);
        OrderController controller = new OrderController(orderService);

        OrderRequest request = new OrderRequest();
        request.setProduct("Notebook");
        request.setQuantity(2);

        OrderResponse serviceResponse = new OrderResponse(
                "Order created successfully",
                "Notebook",
                2,
                "APPROVED",
                "TRUCK-N-2",
                8
        );

        when(orderService.createOrder(request)).thenReturn(serviceResponse);

        controller.createOrder(request);

        verify(orderService, times(1)).createOrder(request);
    }

    @Test
    void shouldPropagateExceptionWhenServiceRejectsOrder() {
        OrderService orderService = mock(OrderService.class);
        OrderController controller = new OrderController(orderService);

        OrderRequest request = new OrderRequest();
        request.setProduct("Notebook");
        request.setQuantity(20);

        when(orderService.createOrder(request))
                .thenThrow(new OrderRejectedException("OUT_OF_STOCK"));

        assertThrows(OrderRejectedException.class, () -> controller.createOrder(request));
    }
}