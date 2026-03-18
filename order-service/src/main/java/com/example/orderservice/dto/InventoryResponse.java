package com.example.orderservice.dto;

import lombok.Data;

@Data
public class InventoryResponse {

    private String status;
    private String truckId;
    private String reason;

    public InventoryResponse() {
    }

}