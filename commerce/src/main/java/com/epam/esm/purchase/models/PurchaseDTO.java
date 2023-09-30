package com.epam.esm.purchase.models;

import com.epam.esm.purchasecertificate.model.PurchaseCertificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.epam.esm.utils.Constants.DATE_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private Long id;
    private String description;
    private BigDecimal cost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime lastUpdateDate;
    private Long userId;
    private List<PurchaseCertificate> purchaseCertificates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PurchaseDTO order = (PurchaseDTO) o;
        return id != null && Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}