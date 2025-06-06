package com.ucan.backend.post;

public record NewCommentCreated(
    Long postId, String postTitle, Long postAuthorId, String commentAuthorUsername) {}
