package com.epam.esm.tag.repository;

import com.epam.esm.tag.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> getTagsByNameContaining(String namePart, Pageable pageable);
}