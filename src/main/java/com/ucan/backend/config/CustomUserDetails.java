package com.ucan.backend.config;

import com.ucan.backend.userauth.BadgeDTO;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {
  private final Long userId;
  private final String username;
  private final String password;
  private final boolean enabled;
  private final Collection<? extends GrantedAuthority> authorities;
  private final List<BadgeDTO> badges;

  public CustomUserDetails(
      Long userId, String username, String password, boolean enabled, List<BadgeDTO> badges) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.enabled = enabled;
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    this.badges = badges;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
