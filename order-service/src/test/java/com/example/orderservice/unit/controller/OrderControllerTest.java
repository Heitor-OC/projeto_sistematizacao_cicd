package com.example.orderservice.unit.controller;

import com.example.orderservice.controller.OrderController;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldReturnOkWhenRequestIsValid() throws Exception {
        doNothing().when(orderService).createOrder(any());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Notebook",
                          "quantity": 2
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCallServiceWhenRequestIsValid() throws Exception {
        doNothing().when(orderService).createOrder(any());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Mouse",
                          "quantity": 1
                        }
                        """))
                .andExpect(status().isOk());

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void shouldReturnBadRequestWhenProductIsMissing() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "quantity": 1
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsMissing() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Notebook"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnServerErrorWhenServiceThrowsException() throws Exception {
        doThrow(new RuntimeException("Order rejected"))
                .when(orderService).createOrder(any());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Notebook",
                          "quantity": 20
                        }
                        """))
                .andExpect(status().is5xxServerError());
    }
}