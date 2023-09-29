package com.epam.esm.utils;

import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.models.TagDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    TagDTO toTagDTO(Tag tag);

    Tag toTag(TagDTO tagDTO);
}