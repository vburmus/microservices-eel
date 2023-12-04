package com.epam.esm.purchase.service;

import com.epam.esm.purchase.models.PurchaseCreationRequest;
import com.epam.esm.purchase.models.PurchaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseService {
    PurchaseDTO create(PurchaseCreationRequest purchaseCreationRequest);

    Page<PurchaseDTO> readAll(Pageable pageable);

    PurchaseDTO getById(long id);
    Page<PurchaseDTO> getAllByUserId(long userId, Pageable pageable);
}