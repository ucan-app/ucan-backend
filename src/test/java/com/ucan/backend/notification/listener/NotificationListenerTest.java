package com.ucan.backend.notification.listener;

import static org.mockito.Mockito.*;

import com.ucan.backend.notification.service.NotificationService;
import com.ucan.backend.post.NewCommentCreated;
import com.ucan.backend.post.NewReplyCreated;
import org.junit.jupiter.api.Test;

class NotificationListenerTest {

  @Test
  void handleNewComment_ShouldTriggerNotification() {
    NotificationService mockService = mock(NotificationService.class);
    NotificationListener listener = new NotificationListener(mockService);

    NewCommentCreated event = new NewCommentCreated(1L, "Post title", 42L);
    listener.handleNewComment(event);

    verify(mockService)
        .sendNotification(42L, "Someone commented on your post: Post title", 1L, null);
  }

  @Test
  void handleNewReply_ShouldTriggerNotification() {
    NotificationService mockService = mock(NotificationService.class);
    NotificationListener listener = new NotificationListener(mockService);

    NewReplyCreated event = new NewReplyCreated(123L, 88L, "This is a reply", 999L);
    listener.handleNewReply(event);

    verify(mockService)
        .sendNotification(88L, "Someone replied to your comment: This is a reply", 999L, 123L);
  }

  @Test
  void handleNewReply_ShouldTruncateLongContent() {
    NotificationService mockService = mock(NotificationService.class);
    NotificationListener listener = new NotificationListener(mockService);

    String longReply =
        "This is a very long reply that should be truncated to avoid overflowing the notification.";
    NewReplyCreated event = new NewReplyCreated(123L, 88L, longReply, 999L);
    listener.handleNewReply(event);

    verify(mockService)
        .sendNotification(
            eq(88L),
            startsWith("Someone replied to your comment: This is a very long"),
            eq(999L),
            eq(123L));
  }
}
