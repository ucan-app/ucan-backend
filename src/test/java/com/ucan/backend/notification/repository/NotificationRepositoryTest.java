package com.ucan.backend.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.notification.model.NotificationEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Transactional
class NotificationRepositoryTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @DynamicPropertySource
  static void setDatasourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired private NotificationRepository repository;

  @Test
  void shouldReturnNotificationsSortedByCreatedAtDesc() {
    // Given
    Long recipientId = 1L;

    NotificationEntity older = new NotificationEntity();
    older.setRecipientId(recipientId);
    older.setMessage("Old Notification");
    older.setCreatedAt(LocalDateTime.now().minusHours(1));
    repository.save(older);

    NotificationEntity newer = new NotificationEntity();
    newer.setRecipientId(recipientId);
    newer.setMessage("New Notification");
    newer.setCreatedAt(LocalDateTime.now());
    repository.save(newer);

    // When
    List<NotificationEntity> results =
        repository.findByRecipientIdOrderByCreatedAtDesc(recipientId);

    // Then
    assertThat(results).hasSize(2);
    assertThat(results.get(0).getMessage()).isEqualTo("New Notification");
    assertThat(results.get(1).getMessage()).isEqualTo("Old Notification");
  }
}
