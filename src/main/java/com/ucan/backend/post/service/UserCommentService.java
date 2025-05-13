package com.ucan.backend.post.service;

import com.ucan.backend.post.UserCommentAPI;
import com.ucan.backend.post.UserCommentDTO;
import com.ucan.backend.post.mapper.UserCommentMapper;
import com.ucan.backend.post.model.UserCommentEntity;
import com.ucan.backend.post.repository.UserCommentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommentService implements UserCommentAPI {

  private final UserCommentRepository repository;
  private final UserCommentMapper mapper;

  @Override
  public UserCommentDTO createComment(Long postId, UserCommentDTO dto) {
    UserCommentEntity entity = mapper.toEntity(dto);
    entity.setPostId(postId);
    return mapper.toDTO(repository.save(entity));
  }

  @Override
  public List<UserCommentDTO> getCommentsByPostId(Long postId) {
    return repository.findByPostId(postId).stream().map(mapper::toDTO).collect(Collectors.toList());
  }

  @Override
  public UserCommentDTO getCommentById(Long commentId) {
    return repository
        .findById(commentId)
        .map(mapper::toDTO)
        .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
  }

  @Override
  public boolean existsById(Long commentId) {
    return repository.existsById(commentId);
  }
}
