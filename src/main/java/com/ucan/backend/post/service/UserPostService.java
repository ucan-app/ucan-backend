package com.ucan.backend.post.service;

import com.ucan.backend.post.NewPostCreated;
import com.ucan.backend.post.UserPostAPI;
import com.ucan.backend.post.UserPostDTO;
import com.ucan.backend.post.mapper.UserPostMapper;
import com.ucan.backend.post.model.UserPostEntity;
import com.ucan.backend.post.repository.UserPostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
  public UserPostDTO createPost(String title, String description, Long creatorId) {
    UserPostEntity post = new UserPostEntity();
    post.setTitle(title);
    post.setUpvote(0);
    post.setDownvote(0);
    post.setDescription(description);
    post.setCreatorId(creatorId);
    UserPostEntity savedPost = postRepository.save(post);
    UserPostDTO postDTO = postMapper.toDTO(savedPost);

    eventPublisher.publishEvent(
        new NewPostCreated(
            savedPost.getId(),
            savedPost.getTitle(),
            savedPost.getUpvote(),
            savedPost.getDownvote(),
            savedPost.getCreatorId(),
            savedPost.getCreatedAt()));

    return postDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public UserPostDTO getPost(Long postId) {
    return postRepository
        .findById(postId)
        .map(postMapper::toDTO)
        .orElseThrow(() -> new RuntimeException("Post not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserPostDTO> getPostsByCreator(Long creatorId) {
    return postRepository.findByCreatorId(creatorId).stream().map(postMapper::toDTO).toList();
  }

  @Override
  @Transactional
  public void deletePost(Long postId) {
    postRepository.deleteById(postId);
  }

  @Override
  @Transactional
  public UserPostDTO updatePost(Long postId, String title, String description) {
    UserPostEntity post =
        postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

    post.setTitle(title);
    post.setDescription(description);

    return postMapper.toDTO(postRepository.save(post));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserPostDTO> getAllPosts(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return postRepository.findAllByOrderByCreatedAtDesc(pageRequest).map(postMapper::toDTO);
  }

  // TODO: Implement these methods when post functionality is added
  @Override
  public List<UserPostDTO> getPostsByTag(String tag) {
    return List.of();
  }

  @Transactional
  public void upvotePost(Long postId) {
    UserPostEntity post =
        postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
    post.setUpvote(post.getUpvote() + 1);
    postRepository.save(post);
  }

  @Transactional
  public void downvotePost(Long postId) {
    UserPostEntity post =
        postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
    post.setDownvote(post.getDownvote() + 1);
    postRepository.save(post);
  }
}
