package com.ucan.backend.post;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

public interface UserPostAPI {
  UserPostDTO createPost(String title, String description, Long creatorId);

  UserPostDTO createPostWithTags(
      String title, String description, Long creatorId, Set<Long> tagIds);

  UserPostDTO getPost(Long postId);

  List<UserPostDTO> getPostsByCreator(Long creatorId);

  void deletePost(Long postId);

  UserPostDTO updatePost(Long postId, String title, String description);

  UserPostDTO updatePostWithTags(Long postId, String title, String description, Set<Long> tagIds);

  UserPostDTO updatePostImage(Long postId, String imageUrl);

  Page<UserPostDTO> getAllPosts(int page, int size);

  List<UserPostDTO> getPostsByTag(String tag);

  List<UserPostDTO> getPostsByTagIds(Set<Long> tagIds);

  void upvotePost(Long postId, Long userId);

  void downvotePost(Long postId, Long userId);
}
