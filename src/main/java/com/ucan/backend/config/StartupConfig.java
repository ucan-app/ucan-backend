package com.ucan.backend.config;

import com.ucan.backend.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StartupConfig {

  private final TagService tagService;

  @Bean
  public ApplicationRunner initializeData() {
    return args -> {
      try {
        tagService.initializePredefinedTags();
        System.out.println("Predefined tags initialized successfully!");
      } catch (Exception e) {
        System.err.println("Failed to initialize predefined tags: " + e.getMessage());
      }
    };
  }
}
