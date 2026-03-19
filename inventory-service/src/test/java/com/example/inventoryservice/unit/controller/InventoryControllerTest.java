package com.example.inventoryservice.unit.controller;

import com.example.inventoryservice.controller.InventoryController;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Test
    void shouldReturnOkWhenRequestIsValid() throws Exception {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");
        response.setTruckId("TRUCK-N-2");
        response.setProduct("Notebook");
        response.setRequestedQuantity(2);
        response.setRemainingStock(8);

        when(inventoryService.process(any())).thenReturn(response);

        mockMvc.perform(post("/inventory")
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
    void shouldReturnApprovedResponseBody() throws Exception {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");
        response.setTruckId("TRUCK-M-1");
        response.setProduct("Mouse");
        response.setRequestedQuantity(1);
        response.setRemainingStock(29);

        when(inventoryService.process(any())).thenReturn(response);

        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Mouse",
                          "quantity": 1
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.truckId").value("TRUCK-M-1"))
                .andExpect(jsonPath("$.product").value("Mouse"))
                .andExpect(jsonPath("$.requestedQuantity").value(1))
                .andExpect(jsonPath("$.remainingStock").value(29));
    }

    @Test
    void shouldReturnRejectedResponseBody() throws Exception {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("REJECTED");
        response.setReason("OUT_OF_STOCK");
        response.setProduct("Notebook");
        response.setRequestedQuantity(50);
        response.setRemainingStock(10);

        when(inventoryService.process(any())).thenReturn(response);

        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Notebook",
                          "quantity": 50
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.reason").value("OUT_OF_STOCK"))
                .andExpect(jsonPath("$.product").value("Notebook"))
                .andExpect(jsonPath("$.requestedQuantity").value(50))
                .andExpect(jsonPath("$.remainingStock").value(10));
    }

    @Test
    void shouldReturnBadRequestWhenProductIsMissing() throws Exception {
        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "quantity": 2
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsMissing() throws Exception {
        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "Keyboard"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}