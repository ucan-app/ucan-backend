package com.ucan.backend.post;

import java.time.LocalDateTime;

public record UserPostDTO(
    Long id,
    String title,
    int upvote,
    int downvote,
    String description,
    Long creatorId,
    String imageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
