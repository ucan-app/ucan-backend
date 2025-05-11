package com.ucan.backend.forum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ucan.backend.forum.NewForumCreated;
import com.ucan.backend.forum.UserForumDTO;
import com.ucan.backend.forum.mapper.UserForumMapper;
import com.ucan.backend.forum.model.UserForumEntity;
import com.ucan.backend.forum.repository.UserForumRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class UserForumServiceTest {

  @Mock private UserForumRepository forumRepository;
  @Mock private UserForumMapper forumMapper;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Captor private ArgumentCaptor<NewForumCreated> eventCaptor;

  private UserForumService forumService;

  @BeforeEach
  void setUp() {
    forumService = new UserForumService(forumRepository, forumMapper, eventPublisher);
  }

  @Test
  void createForum_ShouldCreateAndPublishEvent() {
    // Given
    String title = "Test Forum";
    String description = "Test Description";
    UUID creatorId = UUID.randomUUID();
    UUID forumId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    UserForumEntity entity = new UserForumEntity();
    entity.setId(forumId);
    entity.setTitle(title);
    entity.setDescription(description);
    entity.setCreatorId(creatorId);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    UserForumDTO dto = new UserForumDTO(forumId, title, description, creatorId, now, now);

    when(forumRepository.save(any(UserForumEntity.class))).thenReturn(entity);
    when(forumMapper.toDTO(entity)).thenReturn(dto);

    // When
    UserForumDTO result = forumService.createForum(title, description, creatorId);

    // Then
    assertThat(result).isEqualTo(dto);
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    NewForumCreated event = eventCaptor.getValue();
    assertThat(event.forumId()).isEqualTo(forumId);
    assertThat(event.title()).isEqualTo(title);
    assertThat(event.creatorId()).isEqualTo(creatorId);
  }

  @Test
  void getForum_WhenExists_ShouldReturnForum() {
    // Given
    UUID forumId = UUID.randomUUID();
    UserForumEntity entity = new UserForumEntity();
    UserForumDTO dto =
        new UserForumDTO(
            forumId, "Test", "Desc", UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now());

    when(forumRepository.findById(forumId)).thenReturn(Optional.of(entity));
    when(forumMapper.toDTO(entity)).thenReturn(dto);

    // When
    UserForumDTO result = forumService.getForum(forumId);

    // Then
    assertThat(result).isEqualTo(dto);
  }

  @Test
  void getForum_WhenNotExists_ShouldThrowException() {
    // Given
    UUID forumId = UUID.randomUUID();
    when(forumRepository.findById(forumId)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> forumService.getForum(forumId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Forum not found");
  }

  @Test
  void getForumsByCreator_ShouldReturnList() {
    // Given
    UUID creatorId = UUID.randomUUID();
    UserForumEntity entity = new UserForumEntity();
    UserForumDTO dto =
        new UserForumDTO(
            UUID.randomUUID(), "Test", "Desc", creatorId, LocalDateTime.now(), LocalDateTime.now());

    when(forumRepository.findByCreatorId(creatorId)).thenReturn(List.of(entity));
    when(forumMapper.toDTO(entity)).thenReturn(dto);

    // When
    List<UserForumDTO> result = forumService.getForumsByCreator(creatorId);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(dto);
  }

  @Test
  void deleteForum_ShouldDelete() {
    // Given
    UUID forumId = UUID.randomUUID();

    // When
    forumService.deleteForum(forumId);

    // Then
    verify(forumRepository).deleteById(forumId);
  }

  @Test
  void updateForum_WhenExists_ShouldUpdate() {
    // Given
    UUID forumId = UUID.randomUUID();
    String newTitle = "New Title";
    String newDescription = "New Description";
    UserForumEntity entity = new UserForumEntity();
    UserForumDTO dto =
        new UserForumDTO(
            forumId,
            newTitle,
            newDescription,
            UUID.randomUUID(),
            LocalDateTime.now(),
            LocalDateTime.now());

    when(forumRepository.findById(forumId)).thenReturn(Optional.of(entity));
    when(forumRepository.save(entity)).thenReturn(entity);
    when(forumMapper.toDTO(entity)).thenReturn(dto);

    // When
    UserForumDTO result = forumService.updateForum(forumId, newTitle, newDescription);

    // Then
    assertThat(result).isEqualTo(dto);
    assertThat(entity.getTitle()).isEqualTo(newTitle);
    assertThat(entity.getDescription()).isEqualTo(newDescription);
  }

  @Test
  void updateForum_WhenNotExists_ShouldThrowException() {
    // Given
    UUID forumId = UUID.randomUUID();
    when(forumRepository.findById(forumId)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> forumService.updateForum(forumId, "New Title", "New Description"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Forum not found");
  }
}
