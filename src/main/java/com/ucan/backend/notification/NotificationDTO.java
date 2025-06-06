package com.ucan.backend.notification;

import java.time.LocalDateTime;

public record NotificationDTO(
    Long id,
    Long recipientId,
    String message,
    boolean read,
    LocalDateTime createdAt,
    Long postId,
    Long commentId) {}
