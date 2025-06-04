package com.ucan.backend.post.service;

import com.ucan.backend.post.NewCommentCreated;
import com.ucan.backend.post.UserCommentAPI;
import com.ucan.backend.post.UserCommentDTO;
import com.ucan.backend.post.mapper.UserCommentMapper;
import com.ucan.backend.post.model.UserCommentEntity;
import com.ucan.backend.post.repository.UserCommentRepository;
import com.ucan.backend.post.repository.UserPostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommentService implements UserCommentAPI {

  private final UserCommentRepository repository;
  private final UserCommentMapper mapper;
  private final ApplicationEventPublisher eventPublisher;
  private final UserPostRepository postRepository;

  @Override
  public UserCommentDTO createComment(Long postId, UserCommentDTO dto) {
    UserCommentEntity entity = mapper.toEntity(dto);
    entity.setPostId(postId);
    UserCommentEntity savedEntity = repository.save(entity);
    UserCommentDTO savedDto = mapper.toDTO(savedEntity);

    String postTitle = getPostTitle(postId);
    eventPublisher.publishEvent(new NewCommentCreated(postId, postTitle, dto.authorId()));

    return savedDto;
  }

  private String getPostTitle(Long postId) {
    return postRepository.findById(postId).map(post -> post.getTitle()).orElse("Unknown Post");
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
