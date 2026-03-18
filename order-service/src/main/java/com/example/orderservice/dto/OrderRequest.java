package com.example.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequest {

    private String product;
    private Integer quantity;

    public OrderRequest() {
    }


}