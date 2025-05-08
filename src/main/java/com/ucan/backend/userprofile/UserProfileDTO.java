package com.ucan.backend.userprofile;

import java.time.Instant;
import java.util.List;

public record UserProfileDTO(
    Long id,
    Long userId,
    String fullName,
    String linkedinUrl,
    String personalWebsite,
    String bio,
    Integer graduationYear,
    List<String> badges,
    Instant createdAt,
    Instant updatedAt) {}
