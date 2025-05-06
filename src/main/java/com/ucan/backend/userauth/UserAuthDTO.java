package com.ucan.backend.userauth;

public record UserAuthDTO(
    Long id, String username, String email, String password, boolean enabled) {}
