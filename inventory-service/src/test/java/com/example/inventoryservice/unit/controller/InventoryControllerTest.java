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
        response.setTruckId("TRUCK-1");

        when(inventoryService.process(any())).thenReturn(response);

        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "borracha",
                          "quantity": 2
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnApprovedResponseBody() throws Exception {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("APPROVED");
        response.setTruckId("TRUCK-1");

        when(inventoryService.process(any())).thenReturn(response);

        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
//                          "product": "caderno",
                          "quantity": 1
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldReturnRejectedResponseBody() throws Exception {
        InventoryResponse response = new InventoryResponse();
        response.setStatus("REJECTED");
        response.setReason("OUT_OF_STOCK");

        when(inventoryService.process(any())).thenReturn(response);

        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "product": "caneta",
                          "quantity": 50
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
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
                          "product": "livro"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}