package com.ucan.backend.gateway.controller;

import com.ucan.backend.post.UserPostAPI;
import com.ucan.backend.post.UserPostDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class UserPostController {
  private final UserPostAPI postService;

  @GetMapping
  public ResponseEntity<Page<UserPostDTO>> getAllPosts(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(postService.getAllPosts(page, size));
  }

  @PostMapping
  public ResponseEntity<UserPostDTO> createPost(
      @RequestParam String title, @RequestParam String description, @RequestParam Long creatorId) {
    return ResponseEntity.ok(postService.createPost(title, description, creatorId));
  }

  @GetMapping("/{postId}")
  public ResponseEntity<UserPostDTO> getPost(@PathVariable Long postId) {
    return ResponseEntity.ok(postService.getPost(postId));
  }

  @GetMapping("/creator/{creatorId}")
  public ResponseEntity<List<UserPostDTO>> getPostsByCreator(@PathVariable Long creatorId) {
    return ResponseEntity.ok(postService.getPostsByCreator(creatorId));
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
    postService.deletePost(postId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{postId}")
  public ResponseEntity<UserPostDTO> updatePost(
      @PathVariable Long postId, @RequestParam String title, @RequestParam String description) {
    return ResponseEntity.ok(postService.updatePost(postId, title, description));
  }

  // It toggles upvote for a post. If user already upvoted, remove it
  @PatchMapping("/{postId}/upvote")
  public ResponseEntity<Void> upvotePost(@PathVariable Long postId, @RequestParam Long userId) {
    postService.upvotePost(postId, userId);
    return ResponseEntity.ok().build();
  }

  // It toggles downvote for a post. If user already downvoted, remove it
  @PatchMapping("/{postId}/downvote")
  public ResponseEntity<Void> downvotePost(@PathVariable Long postId, @RequestParam Long userId) {
    postService.downvotePost(postId, userId);
    return ResponseEntity.ok().build();
  }
}
