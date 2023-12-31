package com.epam.esm.purchase.controllers;

import com.epam.esm.purchase.models.PurchaseCreationRequest;
import com.epam.esm.purchase.models.PurchaseDTO;
import com.epam.esm.purchase.service.PurchaseService;
import jakarta.validation.Valid;
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
    public ResponseEntity<PurchaseDTO> create(@Valid @RequestBody PurchaseCreationRequest purchaseCreationRequest) {
        return ResponseEntity.ok(purchaseService.create(purchaseCreationRequest));
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseDTO>> readAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(purchaseService.readAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(purchaseService.getById(id));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<PurchaseDTO>> getAllByUserId(@PathVariable("id") long userId,
                                                            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(purchaseService.getAllByUserId(userId,pageable));
    }
}