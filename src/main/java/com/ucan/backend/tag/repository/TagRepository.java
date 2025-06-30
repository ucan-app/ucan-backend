package com.ucan.backend.tag.repository;

import com.ucan.backend.tag.model.TagEntity;
import com.ucan.backend.tag.model.TagEntity.TagCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
  List<TagEntity> findByIsActiveTrue();

  List<TagEntity> findByCategoryAndIsActiveTrue(TagCategory category);

  Optional<TagEntity> findByNameIgnoreCase(String name);

  boolean existsByNameIgnoreCase(String name);

  boolean existsByNameIgnoreCaseAndCategory(String name, TagCategory category);
}
