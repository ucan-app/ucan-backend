package com.ucan.backend.forum.mapper;

import com.ucan.backend.forum.UserForumDTO;
import com.ucan.backend.forum.model.UserForumEntity;
import org.springframework.stereotype.Component;

@Component
public class UserForumMapper {
    
    public UserForumDTO toDTO(UserForumEntity entity) {
        return new UserForumDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getCreatorId(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public UserForumEntity toEntity(UserForumDTO dto) {
        UserForumEntity entity = new UserForumEntity();
        entity.setId(dto.id());
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setCreatorId(dto.creatorId());
        entity.setCreatedAt(dto.createdAt());
        entity.setUpdatedAt(dto.updatedAt());
        return entity;
    }
} 