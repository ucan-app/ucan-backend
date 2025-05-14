package com.ucan.backend.post;

import java.time.LocalDateTime;

public record NewPostCreated(Long postId, String title, Long creatorId, LocalDateTime createdAt) {}
