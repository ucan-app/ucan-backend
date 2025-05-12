package com.ucan.backend.post.mapper;

import com.ucan.backend.post.UserReplyDTO;
import com.ucan.backend.post.model.UserReplyEntity;
import org.springframework.stereotype.Component;

@Component
public class UserReplyMapper {

  public UserReplyDTO toDTO(UserReplyEntity entity) {
    return new UserReplyDTO(
        entity.getId(),
        entity.getCommentId(),
        entity.getAuthorId(),
        entity.getContent(),
        entity.getCreatedAt());
  }

  public UserReplyEntity toEntity(UserReplyDTO dto) {
    return UserReplyEntity.builder()
        .id(dto.id())
        .commentId(dto.commentId())
        .authorId(dto.authorId())
        .content(dto.content())
        .createdAt(dto.createdAt())
        .build();
  }
}
