package com.ucan.backend.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.post.model.UserCommentEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserCommentRepositoryTest {

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("pass");

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired private TestEntityManager entityManager;
  @Autowired private UserCommentRepository repository;

  @Test
  void findByPostId_ShouldReturnComments() {
    UserCommentEntity comment =
        UserCommentEntity.builder().postId(100L).authorId(1L).content("Nice post").build();
    entityManager.persist(comment);
    entityManager.flush();

    List<UserCommentEntity> comments = repository.findByPostId(100L);
    assertThat(comments).isNotEmpty();
    assertThat(comments.get(0).getContent()).isEqualTo("Nice post");
  }
}
