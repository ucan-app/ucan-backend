package com.ucan.backend.tag.mapper;

import com.ucan.backend.tag.TagDTO;
import com.ucan.backend.tag.model.TagEntity;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

  public TagDTO toDTO(TagEntity entity) {
    if (entity == null) {
      return null;
    }
    return new TagDTO(
        entity.getId(),
        entity.getName(),
        entity.getCategory(),
        entity.getDescription(),
        entity.isActive());
  }

  public TagEntity toEntity(TagDTO dto) {
    if (dto == null) {
      return null;
    }
    return TagEntity.builder()
        .id(dto.id())
        .name(dto.name())
        .category(dto.category())
        .description(dto.description())
        .isActive(dto.isActive())
        .build();
  }

  public Set<TagDTO> toDTOSet(Set<TagEntity> entities) {
    if (entities == null) {
      return Set.of();
    }
    return entities.stream().map(this::toDTO).collect(Collectors.toSet());
  }

  public Set<TagEntity> toEntitySet(Set<TagDTO> dtos) {
    if (dtos == null) {
      return Set.of();
    }
    return dtos.stream().map(this::toEntity).collect(Collectors.toSet());
  }
}
