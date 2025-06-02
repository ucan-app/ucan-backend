package com.ucan.backend.notification.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.notification.NotificationDTO;
import com.ucan.backend.notification.model.NotificationEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class NotificationMapperTest {

  private final NotificationMapper mapper = new NotificationMapper();

  @Test
  void toDTO_ShouldMapAllFields() {
    NotificationEntity entity = new NotificationEntity();
    entity.setId(1L);
    entity.setRecipientId(10L);
    entity.setMessage("Test");
    entity.setRead(false);
    entity.setCreatedAt(LocalDateTime.now());

    NotificationDTO dto = mapper.toDTO(entity);

    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.recipientId()).isEqualTo(10L);
    assertThat(dto.message()).isEqualTo("Test");
    assertThat(dto.read()).isFalse();
    assertThat(dto.createdAt()).isNotNull();
  }
}
