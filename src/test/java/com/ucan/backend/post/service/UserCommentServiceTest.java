package com.ucan.backend.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ucan.backend.post.UserCommentDTO;
import com.ucan.backend.post.mapper.UserCommentMapper;
import com.ucan.backend.post.model.UserCommentEntity;
import com.ucan.backend.post.repository.UserCommentRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserCommentServiceTest {

  @Mock private UserCommentRepository repository;
  @Mock private UserCommentMapper mapper;

  @InjectMocks private UserCommentService service;

  private UserCommentDTO dto;
  private UserCommentEntity entity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    dto = new UserCommentDTO(1L, 100L, 200L, "Test comment", 0, Instant.now());

    entity =
        UserCommentEntity.builder()
            .id(1L)
            .postId(100L)
            .authorId(200L)
            .content("Test comment")
            .replyCount(0)
            .createdAt(Instant.now())
            .build();
  }

  @Test
  void createComment_ShouldSaveAndReturnDTO() {
    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toDTO(entity)).thenReturn(dto);

    UserCommentDTO result = service.createComment(100L, dto);

    assertNotNull(result);
    verify(repository).save(entity);
  }

  @Test
  void getCommentById_ShouldReturnDTO_WhenFound() {
    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    UserCommentDTO result = service.getCommentById(1L);

    assertEquals(1L, result.id());
  }

  @Test
  void getCommentById_ShouldThrow_WhenNotFound() {
    when(repository.findById(404L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> service.getCommentById(404L));
  }

  @Test
  void existsById_ShouldReturnCorrectValue() {
    when(repository.existsById(1L)).thenReturn(true);
    assertTrue(service.existsById(1L));

    when(repository.existsById(2L)).thenReturn(false);
    assertFalse(service.existsById(2L));
  }

  @Test
  void getCommentsByPostId_ShouldReturnList() {
    when(repository.findByPostId(100L)).thenReturn(List.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    List<UserCommentDTO> comments = service.getCommentsByPostId(100L);
    assertEquals(1, comments.size());
  }
}
