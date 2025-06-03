package com.ucan.backend.gateway.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.ucan.backend.notification.NotificationAPI;
import com.ucan.backend.notification.NotificationDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class NotificationControllerTest {

  @Mock private NotificationAPI notificationAPI;

  @InjectMocks private NotificationController notificationController;

  public NotificationControllerTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getNotifications_ShouldReturnListAndMarkAllAsRead() {
    NotificationDTO dto = new NotificationDTO(1L, 10L, "Test Message", false, LocalDateTime.now());
    when(notificationAPI.getNotificationsAndMarkAsRead(10L)).thenReturn(List.of(dto));

    ResponseEntity<List<NotificationDTO>> response = notificationController.getNotifications(10L);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody()).containsExactly(dto);
    verify(notificationAPI).getNotificationsAndMarkAsRead(10L);
  }

  @Test
  void markAsRead_ShouldReturnOk() {
    ResponseEntity<Void> response = notificationController.markAsRead(1L);
    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    verify(notificationAPI).markAsRead(1L);
  }

  @Test
  void getUnreadCount_ShouldReturnCount() {
    when(notificationAPI.getUnreadCount(10L)).thenReturn(2L);

    ResponseEntity<Long> response = notificationController.getUnreadCount(10L);

    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody()).isEqualTo(2L);
    verify(notificationAPI).getUnreadCount(10L);
  }
}
