package com.ucan.backend.post;

import java.time.LocalDateTime;

public record UserPostDTO(
    Long id,
    String title,
    String description,
    Long creatorId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
