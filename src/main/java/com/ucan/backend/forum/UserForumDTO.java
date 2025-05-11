package com.ucan.backend.forum;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserForumDTO(
    UUID id,
    String title,
    String description,
    UUID creatorId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
