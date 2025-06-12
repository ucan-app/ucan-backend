package com.ucan.backend.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.ucan.backend.notification.NotificationDTO;
import com.ucan.backend.notification.mapper.NotificationMapper;
import com.ucan.backend.notification.model.NotificationEntity;
import com.ucan.backend.notification.repository.NotificationRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationServiceTest {

  private NotificationRepository repository;
  private NotificationService service;
  private NotificationMapper mapper;

  @BeforeEach
  void setUp() {
    repository = mock(NotificationRepository.class);
    mapper = mock(NotificationMapper.class);
    service = new NotificationService(repository, mapper);
  }

  @Test
  void sendNotification_ShouldSaveNotification() {
    service.sendNotification(5L, "Test");

    verify(repository).save(any(NotificationEntity.class));
  }

  @Test
  void getNotifications_ShouldReturnList() {
    NotificationEntity entity = new NotificationEntity();
    NotificationDTO dto = new NotificationDTO(1L, 5L, "Test", false, null);

    when(repository.findByRecipientIdOrderByCreatedAtDesc(5L)).thenReturn(List.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    List<NotificationDTO> result = service.getNotifications(5L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).message()).isEqualTo("Test");
  }

  @Test
  void markAsRead_ShouldUpdateReadStatus() {
    NotificationEntity entity = new NotificationEntity();
    entity.setRead(false);

    when(repository.findById(1L)).thenReturn(Optional.of(entity));

    service.markAsRead(1L);

    assertThat(entity.isRead()).isTrue();
    verify(repository).save(entity);
  }

  @Test
  void markAllAsRead_ShouldUpdateReadAtForAllUnread() {
    NotificationEntity n1 = new NotificationEntity();
    NotificationEntity n2 = new NotificationEntity();

    when(repository.findByRecipientIdAndReadAtIsNull(10L)).thenReturn(List.of(n1, n2));

    service.markAllAsRead(10L);

    assertThat(n1.getReadAt()).isNotNull();
    assertThat(n2.getReadAt()).isNotNull();
    verify(repository).saveAll(List.of(n1, n2));
  }

  @Test
  void countUnreadNotifications_ShouldReturnCorrectCount() {
    when(repository.countByRecipientIdAndReadAtIsNull(10L)).thenReturn(3L);

    long result = service.countUnreadNotifications(10L);

    assertThat(result).isEqualTo(3L);
  }

  @Test
  void getNotificationsAndMarkAsRead_ShouldMarkAndReturnNotifications() {
    NotificationEntity entity = new NotificationEntity();
    NotificationDTO dto = new NotificationDTO(1L, 10L, "Test", false, null);

    when(repository.findByRecipientIdAndReadAtIsNull(10L)).thenReturn(List.of(entity));
    when(repository.findByRecipientIdOrderByCreatedAtDesc(10L)).thenReturn(List.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    List<NotificationDTO> result = service.getNotificationsAndMarkAsRead(10L);

    assertThat(result).hasSize(1);
    verify(repository).saveAll(anyList());
  }
}
