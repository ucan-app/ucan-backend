package com.ucan.backend.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record NewPostCreated(UUID postId, String title, UUID creatorId, LocalDateTime createdAt) {}
