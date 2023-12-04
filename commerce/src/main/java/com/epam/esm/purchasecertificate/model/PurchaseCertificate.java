package com.epam.esm.purchasecertificate.model;

import com.epam.esm.certificate.models.Certificate;
import com.epam.esm.purchase.models.Purchase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "purchase_certificates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PurchaseCertificate {
    @EmbeddedId
    @JsonIgnore
    private PurchaseCertificatePK purchaseCertificatePK;
    @ManyToOne
    @MapsId("certificateId")
    private Certificate certificate;
    @ManyToOne
    @MapsId("purchaseId")
    @JsonIgnore
    private Purchase purchase;
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PurchaseCertificate that = (PurchaseCertificate) o;
        return purchaseCertificatePK != null && Objects.equals(purchaseCertificatePK, that.purchaseCertificatePK);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(purchaseCertificatePK).append(certificate).append(purchase).append(quantity).toHashCode();
    }
}