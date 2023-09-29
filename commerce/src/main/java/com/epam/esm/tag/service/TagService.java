package com.epam.esm.tag.service;


import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.models.TagDTO;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.Validation;
import com.epam.esm.utils.exceptionhandler.exceptions.InvalidTagException;
import com.epam.esm.utils.exceptionhandler.exceptions.NoSuchTagException;
import com.epam.esm.utils.exceptionhandler.exceptions.TagAlreadyExistsException;
import com.epam.esm.utils.openfeign.AwsUtilsFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.epam.esm.utils.Constants.*;


@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final EntityToDtoMapper entityToDtoMapper;
    private final AwsUtilsFeignClient awsClient;
    @Value("${default.image.url}")
    private String defaultImageUrl;

    public Page<TagDTO> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable).map(entityToDtoMapper::toTagDTO);
    }

    @Transactional
    public TagDTO createTag(TagDTO tagDTO, Optional<MultipartFile> image) {
        Tag tag = entityToDtoMapper.toTag(tagDTO);
        if (tagRepository.exists(Example.of(tag)))
            throw new TagAlreadyExistsException(String.format(TAG_ALREADY_EXISTS, tag.getName()));
        if (!Validation.isTagValid(tag)) throw new InvalidTagException(String.format(TAG_IS_INVALID, tag.getName()));
        image.ifPresentOrElse(
                img -> tag.setImageUrl(awsClient.uploadImage(TAGS, img)),
                () -> tag.setImageUrl(defaultImageUrl)
        );
        return entityToDtoMapper.toTagDTO(tagRepository.save(tag));
    }

    public TagDTO getTagById(long id) {
        return tagRepository.findById(id)
                .map(entityToDtoMapper::toTagDTO)
                .orElseThrow(() -> new NoSuchTagException(String.format(TAG_DOESNT_EXIST_ID, id)));
    }

    public void deleteTag(long id) {
        tagRepository.findById(id)
                .ifPresentOrElse(
                        tag -> tagRepository.deleteById(id),
                        () -> {
                            throw new NoSuchTagException(String.format(TAG_DOESNT_EXIST_ID, id));
                        }
                );
    }

    public Page<TagDTO> getTagByNamePart(String namePart, Pageable pageable) {
        return tagRepository.getTagsByNameContaining(namePart, pageable).map(entityToDtoMapper::toTagDTO);
    }
}