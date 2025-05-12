package com.ucan.backend.post;

import java.util.List;

public interface UserCommentAPI {
  UserCommentDTO createComment(Long postId, UserCommentDTO dto);

  List<UserCommentDTO> getCommentsByPostId(Long postId);

  UserCommentDTO getCommentById(Long commentId);

  boolean existsById(Long commentId);
}
