package com.ucan.backend.tag.service;

import com.ucan.backend.tag.TagDTO;
import com.ucan.backend.tag.mapper.TagMapper;
import com.ucan.backend.tag.model.TagEntity;
import com.ucan.backend.tag.model.TagEntity.TagCategory;
import com.ucan.backend.tag.repository.TagRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public List<TagDTO> getAllActiveTags() {
    return tagRepository.findByIsActiveTrue().stream().map(tagMapper::toDTO).toList();
  }

  public List<TagDTO> getTagsByCategory(TagCategory category) {
    return tagRepository.findByCategoryAndIsActiveTrue(category).stream()
        .map(tagMapper::toDTO)
        .toList();
  }

  public Optional<TagDTO> getTagByName(String name) {
    return tagRepository.findByNameIgnoreCase(name).map(tagMapper::toDTO);
  }

  @Transactional
  public TagDTO createTag(TagDTO tagDTO) {
    if (tagRepository.existsByNameIgnoreCase(tagDTO.name())) {
      throw new IllegalArgumentException("Tag with name '" + tagDTO.name() + "' already exists");
    }

    TagEntity entity =
        TagEntity.builder()
            .name(tagDTO.name().trim())
            .category(tagDTO.category())
            .description(tagDTO.description())
            .isActive(true)
            .build();

    TagEntity savedEntity = tagRepository.save(entity);
    return tagMapper.toDTO(savedEntity);
  }

  @Transactional
  public void initializePredefinedTags() {
    createPredefinedTagsIfNotExist();
  }

  private void createPredefinedTagsIfNotExist() {
    // Classes tags
    createTagIfNotExists("CSE 121", TagCategory.CLASSES, "Computer Programming I");
    createTagIfNotExists("CSE 122", TagCategory.CLASSES, "Computer Programming II");
    createTagIfNotExists("CSE 143", TagCategory.CLASSES, "Computer Programming II");
    createTagIfNotExists("CSE 373", TagCategory.CLASSES, "Data Structures and Algorithms");
    createTagIfNotExists("CSE 403", TagCategory.CLASSES, "Software Engineering");
    createTagIfNotExists("CSE 414", TagCategory.CLASSES, "Introduction to Database Systems");
    createTagIfNotExists("CSE 421", TagCategory.CLASSES, "Introduction to Algorithms");
    createTagIfNotExists("CSE 331", TagCategory.CLASSES, "Software Design and Implementation");
    createTagIfNotExists("Others - Classes", TagCategory.CLASSES, "Other classes not listed above");

    // Career tags
    createTagIfNotExists("Internships", TagCategory.CAREERS, "Internship opportunities and advice");
    createTagIfNotExists("Full-time", TagCategory.CAREERS, "Full-time job opportunities");
    createTagIfNotExists(
        "Interview Prep", TagCategory.CAREERS, "Interview preparation tips and resources");
    createTagIfNotExists("Resume Review", TagCategory.CAREERS, "Resume feedback and advice");
    createTagIfNotExists("Career Fair", TagCategory.CAREERS, "Career fair tips and experiences");
    createTagIfNotExists("Networking", TagCategory.CAREERS, "Professional networking advice");
    createTagIfNotExists("Others - Careers", TagCategory.CAREERS, "Other career-related topics");

    // Top tech companies
    createTagIfNotExists("Microsoft", TagCategory.COMPANIES, "Microsoft Corporation");
    createTagIfNotExists("Amazon", TagCategory.COMPANIES, "Amazon.com Inc.");
    createTagIfNotExists("Google", TagCategory.COMPANIES, "Google LLC");
    createTagIfNotExists("Meta", TagCategory.COMPANIES, "Meta Platforms Inc.");
    createTagIfNotExists("Apple", TagCategory.COMPANIES, "Apple Inc.");
    createTagIfNotExists("Netflix", TagCategory.COMPANIES, "Netflix Inc.");
    createTagIfNotExists("Uber", TagCategory.COMPANIES, "Uber Technologies Inc.");
    createTagIfNotExists("Tesla", TagCategory.COMPANIES, "Tesla Inc.");
    createTagIfNotExists("Airbnb", TagCategory.COMPANIES, "Airbnb Inc.");
    createTagIfNotExists("Stripe", TagCategory.COMPANIES, "Stripe Inc.");
    createTagIfNotExists(
        "Others - Companies", TagCategory.COMPANIES, "Other companies not listed above");
  }

  private void createTagIfNotExists(String name, TagCategory category, String description) {
    if (!tagRepository.existsByNameIgnoreCaseAndCategory(name, category)) {
      TagEntity tag =
          TagEntity.builder()
              .name(name)
              .category(category)
              .description(description)
              .isActive(true)
              .build();
      tagRepository.save(tag);
    }
  }

  public Set<TagEntity> getTagEntitiesByIds(Set<Long> tagIds) {
    return Set.copyOf(tagRepository.findAllById(tagIds));
  }

  @Transactional
  public void deleteTag(Long tagId) {
    if (tagRepository.existsById(tagId)) {
      tagRepository.deleteById(tagId);
    } else {
      throw new IllegalArgumentException("Tag with ID " + tagId + " not found");
    }
  }
}
