package com.ucan.backend.userauth;

import java.time.Instant;

public record NewUserCreatedEvent(Long userId, String username, String email, Instant createdAt) {}
