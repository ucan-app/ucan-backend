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
        entity.getCreatedAt());
  }

  public NotificationEntity toEntity(NotificationDTO dto) {
    NotificationEntity entity = new NotificationEntity();
    entity.setId(dto.id());
    entity.setRecipientId(dto.recipientId());
    entity.setMessage(dto.message());
    entity.setRead(dto.read());
    // since createdAt is auto-generated, we don't manually set it
    return entity;
  }
}
