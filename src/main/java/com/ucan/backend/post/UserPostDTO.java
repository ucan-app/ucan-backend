package com.ucan.backend.post;

import com.ucan.backend.tag.TagDTO;
import java.time.LocalDateTime;
import java.util.Set;

public record UserPostDTO(
    Long id,
    String title,
    int upvote,
    int downvote,
    String description,
    Long creatorId,
    String imageUrl,
    Set<TagDTO> tags,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
