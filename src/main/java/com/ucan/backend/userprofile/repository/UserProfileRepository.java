package com.ucan.backend.userprofile.repository;

import com.ucan.backend.userprofile.model.UserProfileEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
  Optional<UserProfileEntity> findByUserId(Long userId);

  boolean existsByUserId(Long userId);
}
