package com.epam.esm.tag.service;


import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.models.TagDTO;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.exceptionhandler.exceptions.NoSuchObjectException;
import com.epam.esm.utils.exceptionhandler.exceptions.ObjectAlreadyExists;
import com.epam.esm.utils.exceptionhandler.exceptions.UpdateException;
import com.epam.esm.utils.openfeign.AwsUtilsFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final EntityToDtoMapper entityToDtoMapper;
    private final AwsUtilsFeignClient awsClient;
    private final ObjectMapper objectMapper;
    @Value("${tag.default.image.url}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public TagDTO create(TagDTO tagDTO, Optional<MultipartFile> image) {
        if (tagRepository.existsByName(tagDTO.name()))
            throw new ObjectAlreadyExists(String.format(TAG_ALREADY_EXISTS, tagDTO.name()));
        Tag tag = entityToDtoMapper.toTag(tagDTO);
        image.ifPresentOrElse(
                img -> tag.setImageUrl(awsClient.uploadImage(TAGS, img)),
                () -> tag.setImageUrl(defaultImageUrl)
        );
        return entityToDtoMapper.toTagDTO(tagRepository.save(tag));
    }

    @Override
    public Page<TagDTO> readAll(Pageable pageable) {
        return tagRepository.findAll(pageable).map(entityToDtoMapper::toTagDTO);
    }

    @Override
    public TagDTO getById(long id) {
        return tagRepository.findById(id)
                .map(entityToDtoMapper::toTagDTO)
                .orElseThrow(() -> new NoSuchObjectException(String.format(TAG_DOESNT_EXIST_ID, id)));
    }

    @Override
    public Page<TagDTO> getByNamePart(String namePart, Pageable pageable) {
        return tagRepository.getByNameContaining(namePart, pageable).map(entityToDtoMapper::toTagDTO);
    }

    @Override
    public void delete(long id) {
        tagRepository.findById(id)
                .ifPresentOrElse(
                        tag -> tagRepository.deleteById(id),
                        () -> {
                            throw new NoSuchObjectException(String.format(TAG_DOESNT_EXIST_ID, id));
                        }
                );
    }

    @Override
    public TagDTO update(long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
            JsonProcessingException {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchObjectException(String.format(CERTIFICATE_DOES_NOT_EXISTS_ID, id)));
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(tag, JsonNode.class));
        Tag updatedTag = objectMapper.treeToValue(patched, Tag.class);
        if (updatedTag == null) throw new UpdateException(UPDATE_TAG_IS_NULL);
        tag.setName(updatedTag.getName());
        if (image != null) tag.setImageUrl(awsClient.uploadImage(TAGS, image));
        return entityToDtoMapper.toTagDTO(tagRepository.save(tag));
    }

    @Override
    public List<Tag> checkTagsAndFetch(List<Tag> tags) {
        List<Tag> fetchedTags = new ArrayList<>();
        for (Tag tag : tags) {
            fetchedTags.add(getByName(tag.getName()));
        }
        return fetchedTags;
    }

    private Tag getByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new NoSuchObjectException(String.format(TAG_DOESNT_EXIST_NAME, name)));
    }
}