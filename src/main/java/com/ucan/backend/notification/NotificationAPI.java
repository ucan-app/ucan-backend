package com.ucan.backend.notification;

import java.util.List;

public interface NotificationAPI {
  List<NotificationDTO> getNotifications(Long userId);

  void markAsRead(Long notificationId);
}
