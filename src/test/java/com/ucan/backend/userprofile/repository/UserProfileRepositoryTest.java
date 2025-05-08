package com.ucan.backend.userprofile.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.userprofile.model.UserProfileEntity;
import java.util.List;
import java.util.Optional;
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
class UserProfileRepositoryTest {

  @Container
  static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("pass");

  @BeforeAll
  static void startContainer() {
    postgres.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
  }

  @Autowired private TestEntityManager entityManager;
  @Autowired private UserProfileRepository repository;

  @Test
  void findByUserId_ShouldReturnProfile_WhenExists() {
    // Arrange
    UserProfileEntity profile =
        UserProfileEntity.builder()
            .userId(42L)
            .fullName("Ali Farhadi")
            .badges(List.of("UW", "Apple"))
            .build();

    entityManager.persistAndFlush(profile);

    // Act
    Optional<UserProfileEntity> result = repository.findByUserId(42L);

    // Assert
    assertThat(result).isPresent();
    assertThat(result.get().getFullName()).isEqualTo("Ali Farhadi");
    assertThat(result.get().getBadges()).containsExactly("UW", "Apple");
  }

  @Test
  void existsByUserId_ShouldReturnTrue_WhenExists() {
    // Arrange
    UserProfileEntity profile =
        UserProfileEntity.builder()
            .userId(100L)
            .fullName("Iman Nilforoush")
            .badges(List.of("Google"))
            .build();

    entityManager.persistAndFlush(profile);

    // Act & Assert
    assertThat(repository.existsByUserId(100L)).isTrue();
  }

  @Test
  void existsByUserId_ShouldReturnFalse_WhenNotExists() {
    assertThat(repository.existsByUserId(999L)).isFalse();
  }
}
