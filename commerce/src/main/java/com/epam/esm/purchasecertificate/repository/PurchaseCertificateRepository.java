package com.epam.esm.purchasecertificate.repository;

import com.epam.esm.purchasecertificate.model.PurchaseCertificate;
import com.epam.esm.purchasecertificate.model.PurchaseCertificatePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCertificateRepository extends JpaRepository<PurchaseCertificate, PurchaseCertificatePK> {
}