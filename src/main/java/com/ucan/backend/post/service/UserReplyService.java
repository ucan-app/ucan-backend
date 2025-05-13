package com.ucan.backend.post.service;

import com.ucan.backend.post.UserReplyAPI;
import com.ucan.backend.post.UserReplyDTO;
import com.ucan.backend.post.mapper.UserReplyMapper;
import com.ucan.backend.post.model.UserReplyEntity;
import com.ucan.backend.post.repository.UserReplyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReplyService implements UserReplyAPI {

  private final UserReplyRepository replyRepository;
  private final UserReplyMapper replyMapper;

  @Override
  public UserReplyDTO createReply(UserReplyDTO replyDTO) {
    UserReplyEntity entity = replyMapper.toEntity(replyDTO);
    UserReplyEntity saved = replyRepository.save(entity);
    return replyMapper.toDTO(saved);
  }

  @Override
  public List<UserReplyDTO> getRepliesByCommentId(Long commentId) {
    return replyRepository.findByCommentId(commentId).stream()
        .map(replyMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public UserReplyDTO getReplyById(Long replyId) {
    return replyRepository
        .findById(replyId)
        .map(replyMapper::toDTO)
        .orElseThrow(() -> new IllegalArgumentException("Reply not found"));
  }

  @Override
  public boolean existsById(Long replyId) {
    return replyRepository.existsById(replyId);
  }
}
