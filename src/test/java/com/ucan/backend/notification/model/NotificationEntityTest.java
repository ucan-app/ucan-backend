package com.ucan.backend.notification.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NotificationEntityTest {

  @Test
  void gettersAndSetters_ShouldWorkCorrectly() {
    NotificationEntity entity = new NotificationEntity();
    entity.setId(1L);
    entity.setRecipientId(20L);
    entity.setMessage("Message");
    entity.setRead(true);

    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getRecipientId()).isEqualTo(20L);
    assertThat(entity.getMessage()).isEqualTo("Message");
    assertThat(entity.isRead()).isTrue();
  }
}
