package com.ucan.backend.userauth.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
  private final Map<String, TokenData> tokenStore = new ConcurrentHashMap<>();
  private final SecureRandom secureRandom = new SecureRandom();

  public String generateToken(String email, String type) {
    byte[] randomBytes = new byte[32];
    secureRandom.nextBytes(randomBytes);
    String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    tokenStore.put(token, new TokenData(email, type));
    return token;
  }

  public TokenData validateAndRemoveToken(String token) {
    return tokenStore.remove(token);
  }

  public record TokenData(String email, String type) {}
}
