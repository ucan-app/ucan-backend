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

  @Test
  void shouldReturnOnlyUnreadNotifications() {
    Long userId = 2L;

    NotificationEntity read = new NotificationEntity();
    read.setRecipientId(userId);
    read.setMessage("Read Notification");
    read.setReadAt(LocalDateTime.now());
    repository.save(read);

    NotificationEntity unread = new NotificationEntity();
    unread.setRecipientId(userId);
    unread.setMessage("Unread Notification");
    unread.setReadAt(null);
    repository.save(unread);

    List<NotificationEntity> unreadResults = repository.findByRecipientIdAndReadAtIsNull(userId);

    assertThat(unreadResults).hasSize(1);
    assertThat(unreadResults.get(0).getMessage()).isEqualTo("Unread Notification");
  }

  @Test
  void shouldCountOnlyUnreadNotifications() {
    Long userId = 3L;

    NotificationEntity n1 = new NotificationEntity();
    n1.setRecipientId(userId);
    n1.setMessage("Unread 1");
    n1.setReadAt(null);
    repository.save(n1);

    NotificationEntity n2 = new NotificationEntity();
    n2.setRecipientId(userId);
    n2.setMessage("Unread 2");
    n2.setReadAt(null);
    repository.save(n2);

    NotificationEntity read = new NotificationEntity();
    read.setRecipientId(userId);
    read.setMessage("Read");
    read.setReadAt(LocalDateTime.now());
    repository.save(read);

    long count = repository.countByRecipientIdAndReadAtIsNull(userId);
    assertThat(count).isEqualTo(2);
  }
}
