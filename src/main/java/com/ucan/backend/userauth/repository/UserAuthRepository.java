package com.ucan.backend.userauth.repository;

import com.ucan.backend.userauth.model.BadgeEntity;
import com.ucan.backend.userauth.model.UserAuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {
  Optional<UserAuthEntity> findByUsername(String username);

  Optional<UserAuthEntity> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  @Query(
      "SELECT b FROM BadgeEntity b WHERE b.id.userId = :userId AND b.id.organizationName = :organizationName")
  Optional<BadgeEntity> findBadgeByUserIdAndOrganization(
      @Param("userId") Long userId, @Param("organizationName") String organizationName);

  @Query(
      "SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BadgeEntity b WHERE b.id.userId = :userId AND b.id.organizationName = :organizationName")
  boolean existsBadgeByUserIdAndOrganization(
      @Param("userId") Long userId, @Param("organizationName") String organizationName);
}
