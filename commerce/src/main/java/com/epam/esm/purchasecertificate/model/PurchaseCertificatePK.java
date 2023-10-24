package com.epam.esm.purchasecertificate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseCertificatePK implements Serializable {
    @Column(name = "purchase_id")
    private Long purchaseId;
    @Column(name = "certificate_id")
    private Long certificateId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseCertificatePK that = (PurchaseCertificatePK) o;
        return Objects.equals(certificateId, that.certificateId) &&
                Objects.equals(purchaseId, that.purchaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, purchaseId);
    }
}