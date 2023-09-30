package com.epam.esm.purchase.controllers;

import com.epam.esm.purchase.models.PurchaseCreationRequest;
import com.epam.esm.purchase.models.PurchaseDTO;
import com.epam.esm.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseDTO> create(@RequestBody PurchaseCreationRequest purchaseCreationRequest) {
        return ResponseEntity.ok(purchaseService.createPurchase(purchaseCreationRequest));
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseDTO>> readAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(purchaseService.readAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseByID(@PathVariable("id") long id) {
        return ResponseEntity.ok(purchaseService.getById(id));
    }
}