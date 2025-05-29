package com.ucan.backend.post.mapper;

import com.ucan.backend.post.UserPostDTO;
import com.ucan.backend.post.model.UserPostEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPostMapper {

  public UserPostDTO toDTO(UserPostEntity entity) {
    return new UserPostDTO(
        entity.getId(),
        entity.getTitle(),
        entity.getUpvote(),
        entity.getDownvote(),
        entity.getDescription(),
        entity.getCreatorId(),
        entity.getImageUrl(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  public UserPostEntity toEntity(UserPostDTO dto) {
    UserPostEntity entity = new UserPostEntity();
    entity.setId(dto.id());
    entity.setTitle(dto.title());
    entity.setUpvote(dto.upvote());
    entity.setDownvote(dto.downvote());
    entity.setDescription(dto.description());
    entity.setCreatorId(dto.creatorId());
    entity.setImageUrl(dto.imageUrl());
    entity.setCreatedAt(dto.createdAt());
    entity.setUpdatedAt(dto.updatedAt());
    return entity;
  }
}
