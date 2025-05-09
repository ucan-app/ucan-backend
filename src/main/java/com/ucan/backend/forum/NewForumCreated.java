package com.ucan.backend.forum;

import java.time.LocalDateTime;
import java.util.UUID;

public record NewForumCreated(
    UUID forumId, String title, UUID creatorId, LocalDateTime createdAt) {}
