package com.ucan.backend.forum.repository;

import com.ucan.backend.forum.model.UserForumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserForumRepository extends JpaRepository<UserForumEntity, UUID> {
    List<UserForumEntity> findByCreatorId(UUID creatorId);
} 