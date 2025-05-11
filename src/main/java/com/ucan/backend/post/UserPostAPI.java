package com.ucan.backend.post;

import java.util.List;
import java.util.UUID;

public interface UserPostAPI {
  UserPostDTO createPost(String title, String description, UUID creatorId);

  UserPostDTO getPost(UUID forumId);

  List<UserPostDTO> getPostsByCreator(UUID creatorId);

  void deletePost(UUID forumId);

  UserPostDTO updatePost(UUID forumId, String title, String description);
}
