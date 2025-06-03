package com.ucan.backend.post;

public record NewReplyCreated(Long commentId, Long commentAuthorId, String replyContent) {}
