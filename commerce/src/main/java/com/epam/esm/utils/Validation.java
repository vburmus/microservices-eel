package com.epam.esm.utils;


import com.epam.esm.certificate.models.Certificate;
import com.epam.esm.tag.models.Tag;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@UtilityClass
public class Validation {
    public static boolean isTagValid(Tag tag) {
        return tag != null && tag.getName() != null && !tag.getName().isEmpty();
    }

    public static boolean isValidCertificate(Certificate giftCertificate) {
        return giftCertificate.getName() != null && !giftCertificate.getName().isEmpty() &&
                giftCertificate.getDurationDate() != null &&
                (LocalDateTime.now()).isBefore(giftCertificate.getDurationDate()) &&
                giftCertificate.getPrice() != null && giftCertificate.getPrice().compareTo(BigDecimal.ZERO) > 0 &&
                giftCertificate.getTags() != null;
    }

}