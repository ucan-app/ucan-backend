package com.ucan.backend.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.post.model.UserReplyEntity;
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
public class UserReplyRepositoryTest {

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("pass");

  @BeforeAll
  static void setup() {
    postgres.start();
  }

  @DynamicPropertySource
  static void setDatasourceProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired private TestEntityManager entityManager;
  @Autowired private UserReplyRepository repository;

  @Test
  void findByCommentId_ShouldReturnReplies() {
    UserReplyEntity reply =
        UserReplyEntity.builder().commentId(1L).authorId(42L).content("Hello reply").build();

    entityManager.persist(reply);
    entityManager.flush();

    List<UserReplyEntity> result = repository.findByCommentId(1L);
    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getContent()).isEqualTo("Hello reply");
  }
}
