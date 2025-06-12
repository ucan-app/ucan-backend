package com.ucan.backend.gateway.controller;

import com.ucan.backend.notification.NotificationAPI;
import com.ucan.backend.notification.NotificationDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationAPI notificationAPI;

  @GetMapping
  public ResponseEntity<List<NotificationDTO>> getNotifications(
      @RequestParam("userId") Long userId) {
    return ResponseEntity.ok(notificationAPI.getNotificationsAndMarkAsRead(userId));
  }

  @GetMapping("/unread-count")
  public ResponseEntity<Long> getUnreadCount(@RequestParam("userId") Long userId) {
    return ResponseEntity.ok(notificationAPI.getUnreadCount(userId));
  }

  @PostMapping("/{notificationId}/read")
  public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
    notificationAPI.markAsRead(notificationId);
    return ResponseEntity.ok().build();
  }
}
