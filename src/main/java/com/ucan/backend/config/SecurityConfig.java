package com.ucan.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucan.backend.userauth.UserAuthAPI;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final UserAuthAPI userAuthAPI;
  private final ObjectMapper objectMapper;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      var userDTO = userAuthAPI.findByUsername(username);
      if (userDTO == null) {
        throw new UsernameNotFoundException("User not found with username: " + username);
      }
      return new CustomUserDetails(
          userDTO.id(), userDTO.username(), userDTO.password(), userDTO.enabled());
    };
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            request ->
                request.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated())
        .formLogin(form -> form.disable())
        .logout(logout -> logout.permitAll())
        .csrf((csrf) -> csrf.disable())
        .authenticationProvider(authenticationProvider());

    return http.build();
  }
}
