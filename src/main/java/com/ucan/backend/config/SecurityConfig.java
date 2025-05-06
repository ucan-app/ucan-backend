package com.ucan.backend.config;

import com.ucan.backend.userauth.UserAuthAPI;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final UserAuthAPI userAuthAPI;

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      var userDTO = userAuthAPI.findByUsername(username);
      if (userDTO == null) {
        throw new UsernameNotFoundException("User not found with username: " + username);
      }
      return new User(
          userDTO.username(),
          userDTO.password(),
          userDTO.enabled(),
          true,
          true,
          true,
          Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            request ->
                request.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated())
        .formLogin(form -> form.permitAll())
        .logout(logout -> logout.permitAll())
        .csrf((csrf) -> csrf.disable());

    return http.build();
  }
}
