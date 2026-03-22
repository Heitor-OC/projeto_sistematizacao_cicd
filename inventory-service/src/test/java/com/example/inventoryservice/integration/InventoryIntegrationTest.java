package com.example.inventoryservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldApproveRequestWhenStockIsAvailable() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": "Mouse",
                  "quantity": 2
                }
                """, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/inventory", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("APPROVED"));
    }

    @Test
    void shouldRejectRequestWhenStockIsInsufficient() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": "Mouse",
                  "quantity": 999
                }
                """, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/inventory", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("REJECTED"));
        assertTrue(response.getBody().contains("OUT_OF_STOCK"));
    }

    @Test
    void shouldReturnBadRequestWhenPayloadIsInvalid() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": null,
                  "quantity": 2
                }
                """, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/inventory", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnStockList() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/inventory/stock", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Mouse"));
        assertTrue(response.getBody().contains("Keyboard"));
    }
}