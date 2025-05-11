package com.ucan.backend.post.service;

import com.ucan.backend.post.NewPostCreated;
import com.ucan.backend.post.UserPostAPI;
import com.ucan.backend.post.UserPostDTO;
import com.ucan.backend.post.mapper.UserPostMapper;
import com.ucan.backend.post.model.UserPostEntity;
import com.ucan.backend.post.repository.UserPostRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPostService implements UserPostAPI {
  private final UserPostRepository postRepository;
  private final UserPostMapper postMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public UserPostDTO createPost(String title, String description, UUID creatorId) {
    UserPostEntity post = new UserPostEntity();
    post.setTitle(title);
    post.setDescription(description);
    post.setCreatorId(creatorId);
    UserPostEntity savedPost = postRepository.save(post);
    UserPostDTO postDTO = postMapper.toDTO(savedPost);

    eventPublisher.publishEvent(
        new NewPostCreated(
            savedPost.getId(),
            savedPost.getTitle(),
            savedPost.getCreatorId(),
            savedPost.getCreatedAt()));

    return postDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public UserPostDTO getPost(UUID postId) {
    return postRepository
        .findById(postId)
        .map(postMapper::toDTO)
        .orElseThrow(() -> new RuntimeException("Post not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserPostDTO> getPostsByCreator(UUID creatorId) {
    return postRepository.findByCreatorId(creatorId).stream().map(postMapper::toDTO).toList();
  }

  @Override
  @Transactional
  public void deletePost(UUID postId) {
    postRepository.deleteById(postId);
  }

  @Override
  @Transactional
  public UserPostDTO updatePost(UUID postId, String title, String description) {
    UserPostEntity post =
        postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

    post.setTitle(title);
    post.setDescription(description);

    return postMapper.toDTO(postRepository.save(post));
  }
}
