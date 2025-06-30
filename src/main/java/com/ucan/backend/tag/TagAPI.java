package com.ucan.backend.tag;

import com.ucan.backend.tag.model.TagEntity.TagCategory;
import java.util.List;

public interface TagAPI {
  List<TagDTO> getAllTags();

  List<TagDTO> getTagsByCategory(TagCategory category);

  TagDTO createTag(TagDTO tagDTO);

  void deleteTag(Long tagId);
}
