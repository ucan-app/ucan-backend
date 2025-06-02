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

  @GetMapping("/{userId}")
  public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long userId) {
    return ResponseEntity.ok(notificationAPI.getNotifications(userId));
  }

  @PostMapping("/{notificationId}/read")
  public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
    notificationAPI.markAsRead(notificationId);
    return ResponseEntity.ok().build();
  }
}
