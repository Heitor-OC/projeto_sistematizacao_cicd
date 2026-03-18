package com.example.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String message;
    private String product;
    private Integer quantity;
    private String status;
    private String truckId;
    private Integer remainingStock;

}
