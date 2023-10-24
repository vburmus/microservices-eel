package com.epam.esm.tag.service;


import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.models.TagDTO;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.exceptionhandler.exceptions.NoSuchObjectException;
import com.epam.esm.utils.exceptionhandler.exceptions.ObjectAlreadyExists;
import com.epam.esm.utils.openfeign.AwsUtilsFeignClient;
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
    @Value("${tag.default.image.url}")
    private String defaultImageUrl;

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

    public Page<TagDTO> readAll(Pageable pageable) {
        return tagRepository.findAll(pageable).map(entityToDtoMapper::toTagDTO);
    }

    public TagDTO getById(long id) {
        return tagRepository.findById(id)
                .map(entityToDtoMapper::toTagDTO)
                .orElseThrow(() -> new NoSuchObjectException(String.format(TAG_DOESNT_EXIST_ID, id)));
    }

    public Page<TagDTO> getByNamePart(String namePart, Pageable pageable) {
        return tagRepository.getByNameContaining(namePart, pageable).map(entityToDtoMapper::toTagDTO);
    }

    public void delete(long id) {
        tagRepository.findById(id)
                .ifPresentOrElse(
                        tag -> tagRepository.deleteById(id),
                        () -> {
                            throw new NoSuchObjectException(String.format(TAG_DOESNT_EXIST_ID, id));
                        }
                );
    }

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