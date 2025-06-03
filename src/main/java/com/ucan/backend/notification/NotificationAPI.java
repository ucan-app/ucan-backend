package com.ucan.backend.notification;

import java.util.List;

public interface NotificationAPI {
  List<NotificationDTO> getNotifications(Long userId);

  void markAsRead(Long notificationId);

  List<NotificationDTO> getNotificationsAndMarkAsRead(Long userId);

  long getUnreadCount(Long userId);
}
