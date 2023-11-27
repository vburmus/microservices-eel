package com.epam.esm.tag.service;


import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.models.TagDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface TagService {
    TagDTO create(TagDTO tagDTO, Optional<MultipartFile> image);

    Page<TagDTO> readAll(Pageable pageable);

    TagDTO getById(long id);

    Page<TagDTO> getByNamePart(String namePart, Pageable pageable);

    void delete(long id);

    TagDTO update(long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
            JsonProcessingException;

    List<Tag> checkTagsAndFetch(List<Tag> tags);
}