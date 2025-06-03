package com.ucan.backend.notification.service;

import com.ucan.backend.notification.NotificationAPI;
import com.ucan.backend.notification.NotificationDTO;
import com.ucan.backend.notification.mapper.NotificationMapper;
import com.ucan.backend.notification.model.NotificationEntity;
import com.ucan.backend.notification.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements NotificationAPI {

  private final NotificationRepository repository;
  private final NotificationMapper mapper;

  public NotificationService(NotificationRepository repository, NotificationMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public void sendNotification(Long recipientId, String message) {
    NotificationEntity notification =
        NotificationEntity.builder().recipientId(recipientId).message(message).build();
    repository.save(notification);
  }

  public List<NotificationDTO> getNotifications(Long userId) {
    return repository.findByRecipientIdOrderByCreatedAtDesc(userId).stream()
        .map(mapper::toDTO)
        .toList();
  }

  public void markAsRead(Long notificationId) {
    repository
        .findById(notificationId)
        .ifPresent(
            n -> {
              n.setRead(true);
              repository.save(n);
            });
  }

  public void markAllAsRead(Long userId) {
    List<NotificationEntity> unreadNotifications =
        repository.findByRecipientIdAndReadAtIsNull(userId);
    if (unreadNotifications.isEmpty()) return;

    for (NotificationEntity notification : unreadNotifications) {
      notification.setReadAt(LocalDateTime.now());
    }
    repository.saveAll(unreadNotifications);
  }

  public long countUnreadNotifications(Long userId) {
    return repository.countByRecipientIdAndReadAtIsNull(userId);
  }

  @Override
  public List<NotificationDTO> getNotificationsAndMarkAsRead(Long userId) {
    markAllAsRead(userId);
    return repository.findByRecipientIdOrderByCreatedAtDesc(userId).stream()
        .map(mapper::toDTO)
        .toList();
  }

  @Override
  public long getUnreadCount(Long userId) {
    return countUnreadNotifications(userId);
  }
}
