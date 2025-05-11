package com.ucan.backend.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserPostDTO(
    UUID id,
    String title,
    String description,
    UUID creatorId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
