package com.ucan.backend.post.repository;

import com.ucan.backend.post.model.UserCommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Long> {
  List<UserCommentEntity> findByPostId(Long postId);
}
