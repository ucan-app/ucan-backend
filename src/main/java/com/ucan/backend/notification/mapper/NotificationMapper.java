package com.ucan.backend.notification.mapper;

import com.ucan.backend.notification.NotificationDTO;
import com.ucan.backend.notification.model.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

  public NotificationDTO toDTO(NotificationEntity entity) {
    return new NotificationDTO(
        entity.getId(),
        entity.getRecipientId(),
        entity.getMessage(),
        entity.isRead(),
        entity.getCreatedAt(),
        entity.getPostId(),
        entity.getCommentId());
  }

  public NotificationEntity toEntity(NotificationDTO dto) {
    return NotificationEntity.builder()
        .id(dto.id())
        .recipientId(dto.recipientId())
        .message(dto.message())
        .read(dto.read())
        .postId(dto.postId())
        .commentId(dto.commentId())
        .build();
  }
}
