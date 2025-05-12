package com.ucan.backend.post.mapper;

import com.ucan.backend.post.UserCommentDTO;
import com.ucan.backend.post.model.UserCommentEntity;
import org.springframework.stereotype.Component;

@Component
public class UserCommentMapper {

  public UserCommentDTO toDTO(UserCommentEntity entity) {
    return new UserCommentDTO(
        entity.getId(),
        entity.getPostId(),
        entity.getAuthorId(),
        entity.getContent(),
        entity.getReplyCount(),
        entity.getCreatedAt());
  }

  public UserCommentEntity toEntity(UserCommentDTO dto) {
    return UserCommentEntity.builder()
        .id(dto.id())
        .postId(dto.postId())
        .authorId(dto.authorId())
        .content(dto.content())
        .replyCount(dto.replyCount())
        .createdAt(dto.createdAt())
        .build();
  }
}
