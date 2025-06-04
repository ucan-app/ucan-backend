package com.ucan.backend.gateway.dto.userprofile;

import com.ucan.backend.userauth.BadgeDTO;
import java.util.List;

public record ProfileResponse(
    Long userId,
    String fullName,
    String linkedinUrl,
    String personalWebsite,
    String bio,
    Integer graduationYear,
    boolean verified,
    List<BadgeDTO> badges) {}
