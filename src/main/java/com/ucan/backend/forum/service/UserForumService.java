package com.ucan.backend.forum.service;

import com.ucan.backend.forum.NewForumCreated;
import com.ucan.backend.forum.UserForumAPI;
import com.ucan.backend.forum.UserForumDTO;
import com.ucan.backend.forum.mapper.UserForumMapper;
import com.ucan.backend.forum.model.UserForumEntity;
import com.ucan.backend.forum.repository.UserForumRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserForumService implements UserForumAPI {
  private final UserForumRepository forumRepository;
  private final UserForumMapper forumMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public UserForumDTO createForum(String title, String description, UUID creatorId) {
    UserForumEntity forum = new UserForumEntity();
    forum.setTitle(title);
    forum.setDescription(description);
    forum.setCreatorId(creatorId);
    UserForumEntity savedForum = forumRepository.save(forum);
    UserForumDTO forumDTO = forumMapper.toDTO(savedForum);

    eventPublisher.publishEvent(
        new NewForumCreated(
            savedForum.getId(),
            savedForum.getTitle(),
            savedForum.getCreatorId(),
            savedForum.getCreatedAt()));

    return forumDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public UserForumDTO getForum(UUID forumId) {
    return forumRepository
        .findById(forumId)
        .map(forumMapper::toDTO)
        .orElseThrow(() -> new RuntimeException("Forum not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserForumDTO> getForumsByCreator(UUID creatorId) {
    return forumRepository.findByCreatorId(creatorId).stream().map(forumMapper::toDTO).toList();
  }

  @Override
  @Transactional
  public void deleteForum(UUID forumId) {
    forumRepository.deleteById(forumId);
  }

  @Override
  @Transactional
  public UserForumDTO updateForum(UUID forumId, String title, String description) {
    UserForumEntity forum =
        forumRepository
            .findById(forumId)
            .orElseThrow(() -> new RuntimeException("Forum not found"));

    forum.setTitle(title);
    forum.setDescription(description);

    return forumMapper.toDTO(forumRepository.save(forum));
  }
}
