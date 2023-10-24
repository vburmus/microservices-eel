package com.epam.esm.tag.repository;

import com.epam.esm.tag.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> getByNameContaining(String namePart, Pageable pageable);

    boolean existsByName(String name);

    Optional<Tag> findByName(String name);
}