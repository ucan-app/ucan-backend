package com.ucan.backend.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.ucan.backend.post.NewReplyCreated;
import com.ucan.backend.post.UserReplyDTO;
import com.ucan.backend.post.mapper.UserReplyMapper;
import com.ucan.backend.post.model.UserCommentEntity;
import com.ucan.backend.post.model.UserReplyEntity;
import com.ucan.backend.post.repository.UserCommentRepository;
import com.ucan.backend.post.repository.UserReplyRepository;
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
class UserReplyServiceTest {

  @Mock private UserReplyRepository repository;
  @Mock private UserReplyMapper mapper;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Mock private UserCommentRepository commentRepository;
  @Captor private ArgumentCaptor<NewReplyCreated> eventCaptor;

  private UserReplyService service;

  @BeforeEach
  void setup() {
    service = new UserReplyService(repository, mapper, eventPublisher, commentRepository);
  }

  @Test
  void createReply_ShouldSaveAndPublishEvent() {
    // Given
    Long commentId = 99L;
    Long authorId = 42L;
    Long commentAuthorId = 88L;
    String content = "Thanks!";
    Instant now = Instant.now();

    UserReplyDTO dto = new UserReplyDTO(null, commentId, authorId, content, now);
    UserReplyEntity entity =
        UserReplyEntity.builder()
            .id(1L)
            .commentId(commentId)
            .authorId(authorId)
            .content(content)
            .createdAt(now)
            .build();
    UserReplyDTO savedDto = new UserReplyDTO(1L, commentId, authorId, content, now);
    UserCommentEntity comment =
        UserCommentEntity.builder().id(commentId).authorId(commentAuthorId).postId(555L).build();

    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toDTO(entity)).thenReturn(savedDto);
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    // When
    UserReplyDTO result = service.createReply(dto);

    // Then
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    NewReplyCreated event = eventCaptor.getValue();
    assertThat(event.commentId()).isEqualTo(commentId);
    assertThat(event.commentAuthorId()).isEqualTo(commentAuthorId);
    assertThat(event.replyContent()).isEqualTo(content);
    assertThat(event.postId()).isEqualTo(555L);
    assertThat(result).isEqualTo(savedDto);
  }

  @Test
  void createReply_ShouldThrowException_WhenCommentNotFound() {
    // Given
    Long commentId = 99L;
    Long authorId = 42L;
    String content = "Thanks!";
    Instant now = Instant.now();

    UserReplyDTO dto = new UserReplyDTO(null, commentId, authorId, content, now);
    UserReplyEntity entity =
        UserReplyEntity.builder()
            .id(1L)
            .commentId(commentId)
            .authorId(authorId)
            .content(content)
            .createdAt(now)
            .build();

    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> service.createReply(dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Comment not found");
  }

  @Test
  void getRepliesByCommentId_ShouldReturnReplies() {
    // Given
    Long commentId = 99L;
    UserReplyEntity entity =
        UserReplyEntity.builder()
            .id(1L)
            .commentId(commentId)
            .authorId(42L)
            .content("Thanks!")
            .createdAt(Instant.now())
            .build();
    UserReplyDTO dto = new UserReplyDTO(1L, commentId, 42L, "Thanks!", entity.getCreatedAt());

    when(repository.findByCommentId(commentId)).thenReturn(List.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    // When
    List<UserReplyDTO> replies = service.getRepliesByCommentId(commentId);

    // Then
    assertThat(replies).hasSize(1);
    assertThat(replies.get(0).content()).isEqualTo("Thanks!");
  }
}
