package com.ucan.backend.notification.listener;

import com.ucan.backend.notification.service.NotificationService;
import com.ucan.backend.post.NewCommentCreated;
import com.ucan.backend.post.NewReplyCreated;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

  private final NotificationService notificationService;

  public NotificationListener(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @ApplicationModuleListener
  public void handleNewComment(NewCommentCreated event) {
    String message = "Someone commented on your post: " + event.postTitle();
    notificationService.sendNotification(event.postAuthorId(), message, event.postId(), null);
  }

  @ApplicationModuleListener
  public void handleNewReply(NewReplyCreated event) {
    String message = "Someone replied to your comment: " + summarize(event.replyContent());
    notificationService.sendNotification(
        event.commentAuthorId(), message, event.postId(), event.commentId());
  }

  private String summarize(String content) {
    return content.length() > 50 ? content.substring(0, 47) + "..." : content;
  }
}
