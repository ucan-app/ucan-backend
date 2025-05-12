package com.ucan.backend.post;

import java.time.Instant;

public record UserCommentDTO(
    Long id, Long postId, Long authorId, String content, int replyCount, Instant createdAt) {}
