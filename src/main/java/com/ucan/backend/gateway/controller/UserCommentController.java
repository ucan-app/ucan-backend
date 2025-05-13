package com.ucan.backend.gateway.controller;

import com.ucan.backend.post.UserCommentAPI;
import com.ucan.backend.post.UserCommentDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class UserCommentController {

  private final UserCommentAPI userCommentAPI;

  @PostMapping
  public ResponseEntity<UserCommentDTO> createComment(
      @PathVariable Long postId, @RequestBody UserCommentDTO dto) {
    return ResponseEntity.ok(userCommentAPI.createComment(postId, dto));
  }

  @GetMapping
  public ResponseEntity<List<UserCommentDTO>> getCommentsByPost(@PathVariable Long postId) {
    return ResponseEntity.ok(userCommentAPI.getCommentsByPostId(postId));
  }
}
