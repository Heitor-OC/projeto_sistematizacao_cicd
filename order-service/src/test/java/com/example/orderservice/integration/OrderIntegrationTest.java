package com.example.orderservice.integration;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.InventoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private InventoryClient inventoryClient;

    @Test
    void shouldCreateOrderSuccessfully() {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");

        when(inventoryClient.processOrder(any())).thenReturn(response);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": "Notebook",
                  "quantity": 2
                }
                """, headers);

        ResponseEntity<String> result =
                restTemplate.postForEntity("/orders", request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void shouldReturnInternalServerErrorWhenOrderIsRejected() {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("REJECTED");
        response.setReason("OUT_OF_STOCK");

        when(inventoryClient.processOrder(any())).thenReturn(response);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": "Notebook",
                  "quantity": 20
                }
                """, headers);

        ResponseEntity<String> result =
                restTemplate.postForEntity("/orders", request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestForInvalidPayload() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": null,
                  "quantity": 2
                }
                """, headers);

        ResponseEntity<String> result =
                restTemplate.postForEntity("/orders", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}