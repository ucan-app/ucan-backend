package com.ucan.backend.post;

import java.time.Instant;

public record UserReplyDTO(
    Long id, Long commentId, Long authorId, String content, Instant createdAt) {}
