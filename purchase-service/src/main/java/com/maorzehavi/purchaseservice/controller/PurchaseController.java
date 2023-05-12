package com.maorzehavi.purchaseservice.controller;

import com.maorzehavi.purchaseservice.model.dto.request.PurchaseRequest;
import com.maorzehavi.purchaseservice.model.dto.response.PurchaseResponse;
import com.maorzehavi.purchaseservice.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody PurchaseRequest purchaseResponse, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(purchaseService.createPurchase(purchaseResponse,httpServletRequest).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create purchase")
        ));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        try {
            return ResponseEntity.ok(purchaseService.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PurchaseResponse>> getAllPurchasesByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(purchaseService.findAllByCustomerId(customerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No purchases found for customer id: " + customerId)
        ));
    }

    @GetMapping("{id}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No purchase found for id: " + id)
        ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePurchaseById(@PathVariable Long id) {
        purchaseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
