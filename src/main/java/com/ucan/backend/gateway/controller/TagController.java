package com.ucan.backend.gateway.controller;

import com.ucan.backend.tag.TagAPI;
import com.ucan.backend.tag.TagDTO;
import com.ucan.backend.tag.model.TagEntity.TagCategory;
import com.ucan.backend.tag.service.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TagController implements TagAPI {

  private final TagService tagService;

  @GetMapping
  @Override
  public List<TagDTO> getAllTags() {
    return tagService.getAllActiveTags();
  }

  @GetMapping("/category/{category}")
  @Override
  public List<TagDTO> getTagsByCategory(@PathVariable TagCategory category) {
    return tagService.getTagsByCategory(category);
  }

  @PostMapping
  @Override
  public TagDTO createTag(@RequestBody TagDTO tagDTO) {
    return tagService.createTag(tagDTO);
  }

  @PostMapping("/initialize")
  public ResponseEntity<String> initializeTags() {
    tagService.initializePredefinedTags();
    return ResponseEntity.ok("Predefined tags initialized successfully");
  }

  @DeleteMapping("/{tagId}")
  @Override
  public void deleteTag(@PathVariable Long tagId) {
    tagService.deleteTag(tagId);
  }
}
