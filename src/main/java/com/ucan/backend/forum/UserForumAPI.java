package com.ucan.backend.forum;

import java.util.List;
import java.util.UUID;

public interface UserForumAPI {
    UserForumDTO createForum(String title, String description, UUID creatorId);
    UserForumDTO getForum(UUID forumId);
    List<UserForumDTO> getForumsByCreator(UUID creatorId);
    void deleteForum(UUID forumId);
    UserForumDTO updateForum(UUID forumId, String title, String description);
} 