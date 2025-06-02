package com.ucan.backend.notification.repository;

import com.ucan.backend.notification.model.NotificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  List<NotificationEntity> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}
