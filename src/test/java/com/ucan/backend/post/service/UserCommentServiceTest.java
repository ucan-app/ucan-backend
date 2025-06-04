package com.ucan.backend.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.ucan.backend.post.NewCommentCreated;
import com.ucan.backend.post.UserCommentDTO;
import com.ucan.backend.post.mapper.UserCommentMapper;
import com.ucan.backend.post.model.UserCommentEntity;
import com.ucan.backend.post.model.UserPostEntity;
import com.ucan.backend.post.repository.UserCommentRepository;
import com.ucan.backend.post.repository.UserPostRepository;
import java.time.Instant;
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
class UserCommentServiceTest {

  @Mock private UserCommentRepository repository;
  @Mock private UserCommentMapper mapper;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Mock private UserPostRepository postRepository;
  @Captor private ArgumentCaptor<NewCommentCreated> eventCaptor;

  private UserCommentService service;

  @BeforeEach
  void setUp() {
    service = new UserCommentService(repository, mapper, eventPublisher, postRepository);
  }

  @Test
  void createComment_ShouldCreateAndPublishEvent() {
    // Given
    Long postId = 1L;
    Long authorId = 42L;
    String content = "Test comment";
    String postTitle = "Test Post";
    Instant now = Instant.now();

    UserCommentDTO dto = new UserCommentDTO(null, postId, authorId, content, 0, now);
    UserCommentEntity entity =
        UserCommentEntity.builder()
            .id(1L)
            .postId(postId)
            .authorId(authorId)
            .content(content)
            .replyCount(0)
            .createdAt(now)
            .build();
    UserCommentDTO savedDto = new UserCommentDTO(1L, postId, authorId, content, 0, now);
    UserPostEntity post = new UserPostEntity();
    post.setId(postId);
    post.setTitle(postTitle);

    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toDTO(entity)).thenReturn(savedDto);
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // When
    UserCommentDTO result = service.createComment(postId, dto);

    // Then
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    NewCommentCreated event = eventCaptor.getValue();
    assertThat(event.postId()).isEqualTo(postId);
    assertThat(event.postTitle()).isEqualTo(postTitle);
    assertThat(event.postAuthorId()).isEqualTo(authorId);
    assertThat(result).isEqualTo(savedDto);
  }

  @Test
  void createComment_ShouldUseUnknownPostTitle_WhenPostNotFound() {
    // Given
    Long postId = 1L;
    Long authorId = 42L;
    String content = "Test comment";
    Instant now = Instant.now();

    UserCommentDTO dto = new UserCommentDTO(null, postId, authorId, content, 0, now);
    UserCommentEntity entity =
        UserCommentEntity.builder()
            .id(1L)
            .postId(postId)
            .authorId(authorId)
            .content(content)
            .replyCount(0)
            .createdAt(now)
            .build();
    UserCommentDTO savedDto = new UserCommentDTO(1L, postId, authorId, content, 0, now);

    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toDTO(entity)).thenReturn(savedDto);
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // When
    UserCommentDTO result = service.createComment(postId, dto);

    // Then
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    NewCommentCreated event = eventCaptor.getValue();
    assertThat(event.postId()).isEqualTo(postId);
    assertThat(event.postTitle()).isEqualTo("Unknown Post");
    assertThat(event.postAuthorId()).isEqualTo(authorId);
    assertThat(result).isEqualTo(savedDto);
  }

  @Test
  void getCommentById_ShouldReturnDTO_WhenFound() {
    // Given
    Long commentId = 1L;
    UserCommentEntity entity =
        UserCommentEntity.builder()
            .id(commentId)
            .postId(100L)
            .authorId(200L)
            .content("Test comment")
            .replyCount(0)
            .createdAt(Instant.now())
            .build();
    UserCommentDTO dto =
        new UserCommentDTO(commentId, 100L, 200L, "Test comment", 0, entity.getCreatedAt());

    when(repository.findById(commentId)).thenReturn(Optional.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    // When
    UserCommentDTO result = service.getCommentById(commentId);

    // Then
    assertThat(result).isEqualTo(dto);
  }

  @Test
  void getCommentById_ShouldThrow_WhenNotFound() {
    // Given
    Long commentId = 404L;
    when(repository.findById(commentId)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> service.getCommentById(commentId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Comment not found");
  }

  @Test
  void existsById_ShouldReturnCorrectValue() {
    // Given
    Long commentId = 1L;
    when(repository.existsById(commentId)).thenReturn(true);

    // When
    boolean exists = service.existsById(commentId);

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void getCommentsByPostId_ShouldReturnList() {
    // Given
    Long postId = 100L;
    UserCommentEntity entity =
        UserCommentEntity.builder()
            .id(1L)
            .postId(postId)
            .authorId(200L)
            .content("Test comment")
            .replyCount(0)
            .createdAt(Instant.now())
            .build();
    UserCommentDTO dto =
        new UserCommentDTO(1L, postId, 200L, "Test comment", 0, entity.getCreatedAt());

    when(repository.findByPostId(postId)).thenReturn(List.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    // When
    List<UserCommentDTO> comments = service.getCommentsByPostId(postId);

    // Then
    assertThat(comments).hasSize(1);
    assertThat(comments.get(0)).isEqualTo(dto);
  }
}
