package com.ucan.backend.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.post.model.UserPostEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Transactional
class UserPostRepositoryTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired private UserPostRepository postRepository;

  @Test
  void findByCreatorId_ShouldReturnPosts() {
    // Given
    Long creatorId = 1L;
    UserPostEntity post1 = createPost("Post 1", "Description 1", creatorId);
    UserPostEntity post2 = createPost("Post 2", "Description 2", creatorId);
    UserPostEntity post3 = createPost("Post 3", "Description 3", 2L);

    // When
    List<UserPostEntity> result = postRepository.findByCreatorId(creatorId);

    // Then
    assertThat(result).hasSize(2);
    assertThat(result)
        .extracting(UserPostEntity::getTitle)
        .containsExactlyInAnyOrder("Post 1", "Post 2");
  }

  private UserPostEntity createPost(String title, String description, Long creatorId) {
    UserPostEntity post = new UserPostEntity();
    post.setTitle(title);
    post.setDescription(description);
    post.setCreatorId(creatorId);
    post.setCreatedAt(LocalDateTime.now());
    post.setUpdatedAt(LocalDateTime.now());
    return postRepository.save(post);
  }
}
