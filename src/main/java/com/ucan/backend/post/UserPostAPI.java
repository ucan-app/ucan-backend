package com.ucan.backend.post;

import java.util.List;
import org.springframework.data.domain.Page;

public interface UserPostAPI {
  UserPostDTO createPost(String title, String description, Long creatorId);

  UserPostDTO getPost(Long postId);

  List<UserPostDTO> getPostsByCreator(Long creatorId);

  void deletePost(Long postId);

  UserPostDTO updatePost(Long postId, String title, String description);

  Page<UserPostDTO> getAllPosts(int page, int size);

  // TODO: Implement these methods when post functionality is added
  List<UserPostDTO> getPostsByTag(String tag);

  // upvotePost and downvotePost to the public API interface for modular access
  void upvotePost(Long postId);

  void downvotePost(Long postId);
}
