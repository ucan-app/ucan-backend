package com.ucan.backend.post.repository;

import com.ucan.backend.post.model.UserPostEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPostEntity, UUID> {
  List<UserPostEntity> findByCreatorId(UUID creatorId);
}
