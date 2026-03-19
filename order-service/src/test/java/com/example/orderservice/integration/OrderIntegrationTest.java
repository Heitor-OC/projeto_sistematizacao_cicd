package com.example.orderservice.integration;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.InventoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryClient inventoryClient;

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");
        response.setProduct("Notebook");
        response.setRequestedQuantity(2);
        response.setTruckId("TRUCK-N-2");
        response.setRemainingStock(8);

        when(inventoryClient.processOrder(any())).thenReturn(response);

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
    void shouldReturnBadRequestWhenOrderIsRejected() throws Exception {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("REJECTED");
        response.setReason("OUT_OF_STOCK");

        when(inventoryClient.processOrder(any())).thenReturn(response);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Notebook",
                          "quantity": 20
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": null,
                          "quantity": 2
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}