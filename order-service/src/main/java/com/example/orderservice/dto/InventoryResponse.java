package com.example.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryResponse {

    private String status;
    private String truckId;
    private String reason;


}