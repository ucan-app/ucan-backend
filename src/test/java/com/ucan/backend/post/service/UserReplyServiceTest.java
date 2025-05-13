package com.ucan.backend.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ucan.backend.post.UserReplyDTO;
import com.ucan.backend.post.mapper.UserReplyMapper;
import com.ucan.backend.post.model.UserReplyEntity;
import com.ucan.backend.post.repository.UserReplyRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class UserReplyServiceTest {

  @Mock private UserReplyRepository repository;
  @Mock private UserReplyMapper mapper;
  @InjectMocks private UserReplyService service;

  private UserReplyDTO dto;
  private UserReplyEntity entity;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    dto = new UserReplyDTO(1L, 99L, 42L, "Thanks!", Instant.now());
    entity =
        UserReplyEntity.builder().id(1L).commentId(99L).authorId(42L).content("Thanks!").build();
  }

  @Test
  void createReply_ShouldSaveAndReturnDTO() {
    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toDTO(entity)).thenReturn(dto);

    UserReplyDTO result = service.createReply(dto);

    assertEquals(dto.content(), result.content());
    verify(repository).save(entity);
  }

  @Test
  void getRepliesByCommentId_ShouldReturnReplies() {
    when(repository.findByCommentId(99L)).thenReturn(List.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    List<UserReplyDTO> replies = service.getRepliesByCommentId(99L);

    assertEquals(1, replies.size());
    assertEquals("Thanks!", replies.get(0).content());
  }
}
