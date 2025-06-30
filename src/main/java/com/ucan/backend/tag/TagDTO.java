package com.ucan.backend.tag;

import com.ucan.backend.tag.model.TagEntity.TagCategory;

public record TagDTO(
    Long id, String name, TagCategory category, String description, boolean isActive) {}
