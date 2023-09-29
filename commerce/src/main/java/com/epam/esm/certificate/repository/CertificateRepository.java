package com.epam.esm.certificate.repository;

import com.epam.esm.certificate.models.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.epam.esm.utils.Constants.GET_GC_BY_TAGS_AND_PART;
import static com.epam.esm.utils.Constants.QUERY_FIND_BY_PART_NAME_OR_DESCRIPTION;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    @Query(QUERY_FIND_BY_PART_NAME_OR_DESCRIPTION)
    Page<Certificate> findByPartialNameOrDescription(@Param("partialNameOrShortDescription") String partialNameOrShortDescription, Pageable pageable);

    Page<Certificate> findByTagsIdIn(List<Long> tags, Pageable pageable);

    @Query(nativeQuery = true, value = GET_GC_BY_TAGS_AND_PART)
    Page<Certificate> findByTagsIdInAndShortDescriptionOrNameContaining(List<Long> tags, String partial,
                                                                        Pageable pageable);
}