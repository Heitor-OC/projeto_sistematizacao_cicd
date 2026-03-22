package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.ProductRepository;
import com.example.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.inventoryservice.model.Product;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductRepository repository;

    @GetMapping("/stock")
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> process(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.process(request));
    }
}