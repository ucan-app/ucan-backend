package com.ucan.backend.gateway.controller;

import com.ucan.backend.post.UserPostAPI;
import com.ucan.backend.post.UserPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class UserPostController {
    private final UserPostAPI postService;

    @PostMapping
    public ResponseEntity<UserPostDTO> createPost(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam UUID creatorId
    ) {
        return ResponseEntity.ok(postService.createPost(title, description, creatorId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<UserPostDTO> getPost(@PathVariable UUID postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<UserPostDTO>> getPostsByCreator(@PathVariable UUID creatorId) {
        return ResponseEntity.ok(postService.getPostsByCreator(creatorId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<UserPostDTO> updatePost(
            @PathVariable UUID postId,
            @RequestParam String title,
            @RequestParam String description
    ) {
        return ResponseEntity.ok(postService.updatePost(postId, title, description));
    }
}