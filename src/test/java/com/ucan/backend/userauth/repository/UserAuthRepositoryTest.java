package com.ucan.backend.userauth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.userauth.model.UserAuthEntity;
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
class UserAuthRepositoryTest {

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
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    registry.add("spring.jpa.show-sql", () -> "true");
    registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    registry.add(
        "spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
  }

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserAuthRepository userAuthRepository;

  @Test
  void findByUsername_ShouldReturnUser_WhenUserExists() {
    // Arrange
    UserAuthEntity user =
        UserAuthEntity.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .enabled(true)
            .build();
    entityManager.persist(user);
    entityManager.flush();

    // Act
    Optional<UserAuthEntity> found = userAuthRepository.findByUsername("testuser");

    // Assert
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("testuser");
    assertThat(found.get().getEmail()).isEqualTo("test@example.com");
  }

  @Test
  void findByUsername_ShouldReturnEmpty_WhenUserDoesNotExist() {
    // Act
    Optional<UserAuthEntity> found = userAuthRepository.findByUsername("nonexistent");

    // Assert
    assertThat(found).isEmpty();
  }

  @Test
  void findByEmail_ShouldReturnUser_WhenUserExists() {
    // Arrange
    UserAuthEntity user =
        UserAuthEntity.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .enabled(true)
            .build();
    entityManager.persist(user);
    entityManager.flush();

    // Act
    Optional<UserAuthEntity> found = userAuthRepository.findByEmail("test@example.com");

    // Assert
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("testuser");
    assertThat(found.get().getEmail()).isEqualTo("test@example.com");
  }

  @Test
  void findByEmail_ShouldReturnEmpty_WhenUserDoesNotExist() {
    // Act
    Optional<UserAuthEntity> found = userAuthRepository.findByEmail("nonexistent@example.com");

    // Assert
    assertThat(found).isEmpty();
  }

  @Test
  void existsByUsername_ShouldReturnTrue_WhenUserExists() {
    // Arrange
    UserAuthEntity user =
        UserAuthEntity.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .enabled(true)
            .build();
    entityManager.persist(user);
    entityManager.flush();

    // Act
    boolean exists = userAuthRepository.existsByUsername("testuser");

    // Assert
    assertThat(exists).isTrue();
  }

  @Test
  void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
    // Act
    boolean exists = userAuthRepository.existsByUsername("nonexistent");

    // Assert
    assertThat(exists).isFalse();
  }

  @Test
  void existsByEmail_ShouldReturnTrue_WhenUserExists() {
    // Arrange
    UserAuthEntity user =
        UserAuthEntity.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .enabled(true)
            .build();
    entityManager.persist(user);
    entityManager.flush();

    // Act
    boolean exists = userAuthRepository.existsByEmail("test@example.com");

    // Assert
    assertThat(exists).isTrue();
  }

  @Test
  void existsByEmail_ShouldReturnFalse_WhenUserDoesNotExist() {
    // Act
    boolean exists = userAuthRepository.existsByEmail("nonexistent@example.com");

    // Assert
    assertThat(exists).isFalse();
  }

  @Test
  void save_ShouldSetCreatedAt() {
    // Arrange
    UserAuthEntity user =
        UserAuthEntity.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .enabled(true)
            .build();

    // Act
    UserAuthEntity saved = userAuthRepository.save(user);

    // Assert
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedAt()).isBeforeOrEqualTo(java.time.Instant.now());
  }
}
