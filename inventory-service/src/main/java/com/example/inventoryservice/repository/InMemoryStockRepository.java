package com.example.inventoryservice.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryStockRepository {

    private final Map<String, Integer> stock = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        stock.put("Notebook", 10);
        stock.put("Mouse", 30);
        stock.put("Keyboard", 20);
        stock.put("Monitor", 8);
    }

    public Integer getAvailableQuantity(String product) {
        return stock.getOrDefault(product, 0);
    }

    public void updateStock(String product, Integer newQuantity) {
        stock.put(product, newQuantity);
    }
}