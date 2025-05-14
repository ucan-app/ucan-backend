package com.ucan.backend.post.repository;

import com.ucan.backend.post.model.UserPostEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPostEntity, Long> {
  List<UserPostEntity> findByCreatorId(Long creatorId);

  Page<UserPostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
