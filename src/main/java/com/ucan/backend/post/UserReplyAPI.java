package com.ucan.backend.post;

import java.util.List;

public interface UserReplyAPI {
  UserReplyDTO createReply(UserReplyDTO replyDTO);

  List<UserReplyDTO> getRepliesByCommentId(Long commentId);

  UserReplyDTO getReplyById(Long replyId);

  boolean existsById(Long replyId);
}
