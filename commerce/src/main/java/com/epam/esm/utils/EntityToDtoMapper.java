package com.epam.esm.utils;

import com.epam.esm.certificate.models.Certificate;
import com.epam.esm.certificate.models.CertificateDTO;
import com.epam.esm.purchase.models.Purchase;
import com.epam.esm.purchase.models.PurchaseDTO;
import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.models.TagDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    TagDTO toTagDTO(Tag tag);

    Tag toTag(TagDTO tagDTO);

    CertificateDTO toCertificateDTO(Certificate giftCertificate);

    Certificate toCertificate(CertificateDTO giftCertificateDTO);

    PurchaseDTO toPurchaseDTO(Purchase purchase);
}