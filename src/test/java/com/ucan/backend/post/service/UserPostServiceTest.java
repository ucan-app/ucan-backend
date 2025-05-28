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

  // makes sure that trying to upvote a non-existent post throws an exception
  @Test
  void upvotePost_WhenPostNotFound_ShouldThrowException() {
    Long postId = 99L;
    Long userId = 10L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> postService.upvotePost(postId, userId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Post not found");
  }

  // makes sure that trying to downvote a non-existent post throws an exception
  @Test
  void downvotePost_WhenPostNotFound_ShouldThrowException() {
    Long postId = 100L;
    Long userId = 11L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> postService.downvotePost(postId, userId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Post not found");
  }

  // Tests adding a new upvote from a user who has not voted before
  @Test
  void upvotePost_WhenNewVote_ShouldAddUpvote() {
    Long postId = 1L;
    Long userId = 42L;
    UserPostEntity post = new UserPostEntity();
    post.setUpvote(0);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    postService.upvotePost(postId, userId);

    assertThat(post.getUpvote()).isEqualTo(1);
    assertThat(post.getUserVotes()).containsEntry(userId, true);
    verify(postRepository).save(post);
  }

  // Tests that an existing upvote by the same user is removed
  @Test
  void upvotePost_WhenAlreadyUpvoted_ShouldRemoveVote() {
    Long postId = 1L;
    Long userId = 42L;
    UserPostEntity post = new UserPostEntity();
    post.setUpvote(1);
    post.getUserVotes().put(userId, true);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    postService.upvotePost(postId, userId);

    assertThat(post.getUpvote()).isEqualTo(0);
    assertThat(post.getUserVotes()).doesNotContainKey(userId);
    verify(postRepository).save(post);
  }

  // Tests switching a downvote to an upvote by the same user
  @Test
  void upvotePost_WhenPreviouslyDownvoted_ShouldSwitchToUpvote() {
    Long postId = 1L;
    Long userId = 42L;
    UserPostEntity post = new UserPostEntity();
    post.setUpvote(0);
    post.setDownvote(1);
    post.getUserVotes().put(userId, false);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    postService.upvotePost(postId, userId);

    assertThat(post.getUpvote()).isEqualTo(1);
    assertThat(post.getDownvote()).isEqualTo(0);
    assertThat(post.getUserVotes()).containsEntry(userId, true);
    verify(postRepository).save(post);
  }

  // Tests adding a new downvote from a user who has not voted before
  @Test
  void downvotePost_WhenNewVote_ShouldAddDownvote() {
    Long postId = 2L;
    Long userId = 55L;
    UserPostEntity post = new UserPostEntity();
    post.setDownvote(0);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    postService.downvotePost(postId, userId);

    assertThat(post.getDownvote()).isEqualTo(1);
    assertThat(post.getUserVotes()).containsEntry(userId, false);
    verify(postRepository).save(post);
  }

  // Tests that an existing downvote by the same user is removed
  @Test
  void downvotePost_WhenAlreadyDownvoted_ShouldRemoveVote() {
    // Tests that an existing downvote by the same user is removed (toggling behavior)
    Long postId = 2L;
    Long userId = 55L;
    UserPostEntity post = new UserPostEntity();
    post.setDownvote(1);
    post.getUserVotes().put(userId, false);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    postService.downvotePost(postId, userId);

    assertThat(post.getDownvote()).isEqualTo(0);
    assertThat(post.getUserVotes()).doesNotContainKey(userId);
    verify(postRepository).save(post);
  }

  // Tests switching an upvote to a downvote by the same user
  @Test
  void downvotePost_WhenPreviouslyUpvoted_ShouldSwitchToDownvote() {
    Long postId = 2L;
    Long userId = 55L;
    UserPostEntity post = new UserPostEntity();
    post.setUpvote(1);
    post.setDownvote(0);
    post.getUserVotes().put(userId, true);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    postService.downvotePost(postId, userId);

    assertThat(post.getDownvote()).isEqualTo(1);
    assertThat(post.getUpvote()).isEqualTo(0);
    assertThat(post.getUserVotes()).containsEntry(userId, false);
    verify(postRepository).save(post);
  }
}
