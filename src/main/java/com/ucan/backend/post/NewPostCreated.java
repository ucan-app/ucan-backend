package com.ucan.backend.post;

import java.time.LocalDateTime;

public record NewPostCreated(
    Long postId, String title, int upvote, int downvote, Long creatorId, LocalDateTime createdAt) {}
