package com.ucan.backend.forum.repository;

import com.ucan.backend.forum.model.UserForumEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserForumRepository extends JpaRepository<UserForumEntity, UUID> {
  List<UserForumEntity> findByCreatorId(UUID creatorId);
}
