package com.ucan.backend.gateway.controller;

import com.ucan.backend.post.UserReplyAPI;
import com.ucan.backend.post.UserReplyDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments/{commentId}/replies")
@RequiredArgsConstructor
public class UserReplyController {

  private final UserReplyAPI userReplyAPI;

  @PostMapping
  public ResponseEntity<UserReplyDTO> createReply(
      @PathVariable Long commentId, @RequestBody UserReplyDTO dto) {
    UserReplyDTO replyToSave =
        new UserReplyDTO(null, commentId, dto.authorId(), dto.content(), null);
    return ResponseEntity.ok(userReplyAPI.createReply(replyToSave));
  }

  @GetMapping
  public ResponseEntity<List<UserReplyDTO>> getReplies(@PathVariable Long commentId) {
    return ResponseEntity.ok(userReplyAPI.getRepliesByCommentId(commentId));
  }
}
