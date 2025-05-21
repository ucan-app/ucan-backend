package com.ucan.backend.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ucan.backend.post.NewPostCreated;
import com.ucan.backend.post.UserPostDTO;
import com.ucan.backend.post.mapper.UserPostMapper;
import com.ucan.backend.post.model.UserPostEntity;
import com.ucan.backend.post.repository.UserPostRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class UserPostServiceTest {

  @Mock private UserPostRepository postRepository;
  @Mock private UserPostMapper postMapper;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Captor private ArgumentCaptor<NewPostCreated> eventCaptor;

  private UserPostService postService;

  @BeforeEach
  void setUp() {
    postService = new UserPostService(postRepository, postMapper, eventPublisher);
  }

  @Test
  void createPost_ShouldCreateAndPublishEvent() {
    // Given
    String title = "Test Post";
    int upvote = 6;
    int downvote = 2;
    String description = "Test Description";
    Long creatorId = 1L;
    Long postId = 1L;
    LocalDateTime now = LocalDateTime.now();

    UserPostEntity entity = new UserPostEntity();
    entity.setId(postId);
    entity.setTitle(title);
    entity.setUpvote(upvote);
    entity.setDownvote(downvote);
    entity.setDescription(description);
    entity.setCreatorId(creatorId);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    UserPostDTO dto =
        new UserPostDTO(postId, title, upvote, downvote, description, creatorId, now, now);

    when(postRepository.save(any(UserPostEntity.class))).thenReturn(entity);
    when(postMapper.toDTO(entity)).thenReturn(dto);

    // When
    UserPostDTO result = postService.createPost(title, description, creatorId);

    // Then
    assertThat(result).isEqualTo(dto);
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    NewPostCreated event = eventCaptor.getValue();
    assertThat(event.postId()).isEqualTo(postId);
    assertThat(event.title()).isEqualTo(title);
    assertThat(event.creatorId()).isEqualTo(creatorId);
  }

  @Test
  void getPost_WhenExists_ShouldReturnPost() {
    // Given
    Long postId = 1L;
    UserPostEntity entity = new UserPostEntity();
    UserPostDTO dto =
        new UserPostDTO(postId, "Test", 6, 2, "Desc", 2L, LocalDateTime.now(), LocalDateTime.now());

    when(postRepository.findById(postId)).thenReturn(Optional.of(entity));
    when(postMapper.toDTO(entity)).thenReturn(dto);

    // When
    UserPostDTO result = postService.getPost(postId);

    // Then
    assertThat(result).isEqualTo(dto);
  }

  @Test
  void getPost_WhenNotExists_ShouldThrowException() {
    // Given
    Long postId = 1L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> postService.getPost(postId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Post not found");
  }

  @Test
  void getPostsByCreator_ShouldReturnList() {
    // Given
    Long creatorId = 1L;
    UserPostEntity entity = new UserPostEntity();
    UserPostDTO dto =
        new UserPostDTO(
            2L, "Test", 6, 2, "Desc", creatorId, LocalDateTime.now(), LocalDateTime.now());

    when(postRepository.findByCreatorId(creatorId)).thenReturn(List.of(entity));
    when(postMapper.toDTO(entity)).thenReturn(dto);

    // When
    List<UserPostDTO> result = postService.getPostsByCreator(creatorId);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(dto);
  }

  @Test
  void deletePost_ShouldDelete() {
    // Given
    Long postId = 1L;

    // When
    postService.deletePost(postId);

    // Then
    verify(postRepository).deleteById(postId);
  }

  @Test
  void updatePost_WhenExists_ShouldUpdate() {
    // Given
    Long postId = 1L;
    String newTitle = "New Title";
    String newDescription = "New Description";
    UserPostEntity entity = new UserPostEntity();
    UserPostDTO dto =
        new UserPostDTO(
            postId, newTitle, 6, 2, newDescription, 2L, LocalDateTime.now(), LocalDateTime.now());

    when(postRepository.findById(postId)).thenReturn(Optional.of(entity));
    when(postRepository.save(entity)).thenReturn(entity);
    when(postMapper.toDTO(entity)).thenReturn(dto);

    // When
    UserPostDTO result = postService.updatePost(postId, newTitle, newDescription);

    // Then
    assertThat(result).isEqualTo(dto);
    assertThat(entity.getTitle()).isEqualTo(newTitle);
    assertThat(entity.getDescription()).isEqualTo(newDescription);
  }

  @Test
  void updatePost_WhenNotExists_ShouldThrowException() {
    // Given
    Long postId = 1L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> postService.updatePost(postId, "New Title", "New Description"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Post not found");
  }

  @Test
  void upvotePost_ShouldIncrementUpvote() {
    // Given
    Long postId = 1L;
    UserPostEntity post = new UserPostEntity();
    post.setUpvote(2);
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // When
    postService.upvotePost(postId);

    // Then
    assertThat(post.getUpvote()).isEqualTo(3);
    verify(postRepository).save(post);
  }

  @Test
  void downvotePost_ShouldIncrementDownvote() {
    // Given
    Long postId = 2L;
    UserPostEntity post = new UserPostEntity();
    post.setDownvote(4);
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // When
    postService.downvotePost(postId);

    // Then
    assertThat(post.getDownvote()).isEqualTo(5);
    verify(postRepository).save(post);
  }

  @Test
  void upvotePost_WhenPostNotFound_ShouldThrowException() {
    // Given
    Long postId = 99L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // Then
    assertThatThrownBy(() -> postService.upvotePost(postId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Post not found");
  }

  @Test
  void downvotePost_WhenPostNotFound_ShouldThrowException() {
    // Given
    Long postId = 100L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // Then
    assertThatThrownBy(() -> postService.downvotePost(postId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Post not found");
  }
}
