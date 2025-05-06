package com.ucan.backend.userauth.repository;

import com.ucan.backend.userauth.model.UserAuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {
  Optional<UserAuthEntity> findByUsername(String username);

  Optional<UserAuthEntity> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
