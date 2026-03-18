package com.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private String status;
    private String truckId;
    private String reason;
    private String product;
    private Integer requestedQuantity;
    private Integer remainingStock;

}