package com.example.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryRequest {

    private String product;
    private Integer quantity;

    public InventoryRequest() {
    }

}