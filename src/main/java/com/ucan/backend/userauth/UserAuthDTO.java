package com.ucan.backend.userauth;

import java.util.List;

public record UserAuthDTO(
    Long id,
    String username,
    String email,
    String password,
    boolean enabled,
    List<BadgeDTO> badges) {}
