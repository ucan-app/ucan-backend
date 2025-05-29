package com.ucan.backend.gateway.controller;

import com.ucan.backend.post.UserPostAPI;
import com.ucan.backend.post.UserPostDTO;
import com.ucan.backend.post.service.PostImageService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class UserPostController {
  private final UserPostAPI postService;
  private final PostImageService postImageService;

  @GetMapping
  public ResponseEntity<Page<UserPostDTO>> getAllPosts(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(postService.getAllPosts(page, size));
  }

  @PostMapping
  public ResponseEntity<UserPostDTO> createPost(
      @RequestParam String title,
      @RequestParam String description,
      @RequestParam Long creatorId,
      @RequestParam(required = false) MultipartFile image) {
    UserPostDTO post = postService.createPost(title, description, creatorId);

    if (image != null && !image.isEmpty()) {
      try {
        String imageUrl = postImageService.uploadPostImage(image, post.id());
        post = postService.updatePostImage(post.id(), imageUrl);
      } catch (IOException e) {
        return ResponseEntity.badRequest().build();
      }
    }

    return ResponseEntity.ok(post);
  }

  @PostMapping("/{postId}/image")
  public ResponseEntity<UserPostDTO> uploadPostImage(
      @PathVariable Long postId, @RequestParam("file") MultipartFile file) {
    try {
      String imageUrl = postImageService.uploadPostImage(file, postId);
      UserPostDTO updatedPost = postService.updatePostImage(postId, imageUrl);
      return ResponseEntity.ok(updatedPost);
    } catch (IOException e) {
      return ResponseEntity.badRequest().build();
    }
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
