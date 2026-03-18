package com.example.inventoryservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldProcessApprovedRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": "Notebook",
                  "quantity": 2
                }
                """, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/inventory", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldProcessRejectedRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("""
                {
                  "product": "Notebook",
                  "quantity": 50
                }
                """, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/inventory", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
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

        ResponseEntity<String> response =
                restTemplate.postForEntity("/inventory", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}