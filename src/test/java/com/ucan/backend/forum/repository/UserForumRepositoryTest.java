package com.ucan.backend.forum.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.forum.model.UserForumEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserForumRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserForumRepository forumRepository;

  @Test
  void findByCreatorId_ShouldReturnForums() {
    // Given
    UUID creatorId = UUID.randomUUID();
    UserForumEntity forum1 = createForum("Forum 1", "Description 1", creatorId);
    UserForumEntity forum2 = createForum("Forum 2", "Description 2", creatorId);
    UserForumEntity forum3 = createForum("Forum 3", "Description 3", UUID.randomUUID());

    // When
    List<UserForumEntity> result = forumRepository.findByCreatorId(creatorId);

    // Then
    assertThat(result).hasSize(2);
    assertThat(result)
        .extracting(UserForumEntity::getTitle)
        .containsExactlyInAnyOrder("Forum 1", "Forum 2");
  }

  private UserForumEntity createForum(String title, String description, UUID creatorId) {
    UserForumEntity forum = new UserForumEntity();
    forum.setTitle(title);
    forum.setDescription(description);
    forum.setCreatorId(creatorId);
    forum.setCreatedAt(LocalDateTime.now());
    forum.setUpdatedAt(LocalDateTime.now());
    return entityManager.persist(forum);
  }
}
