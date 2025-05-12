package com.ucan.backend.post.repository;

import com.ucan.backend.post.model.UserReplyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReplyRepository extends JpaRepository<UserReplyEntity, Long> {
  List<UserReplyEntity> findByCommentId(Long commentId);
}
