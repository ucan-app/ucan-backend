package com.ucan.backend.post.service;

import com.ucan.backend.post.NewReplyCreated;
import com.ucan.backend.post.UserReplyAPI;
import com.ucan.backend.post.UserReplyDTO;
import com.ucan.backend.post.mapper.UserReplyMapper;
import com.ucan.backend.post.model.UserReplyEntity;
import com.ucan.backend.post.repository.UserCommentRepository;
import com.ucan.backend.post.repository.UserReplyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReplyService implements UserReplyAPI {

  private final UserReplyRepository replyRepository;
  private final UserReplyMapper replyMapper;
  private final ApplicationEventPublisher eventPublisher;
  private final UserCommentRepository commentRepository;

  @Override
  public UserReplyDTO createReply(UserReplyDTO replyDTO) {
    UserReplyEntity entity = replyMapper.toEntity(replyDTO);
    UserReplyEntity saved = replyRepository.save(entity);
    UserReplyDTO savedDto = replyMapper.toDTO(saved);

    var commentInfo = getCommentInfo(saved.getCommentId());
    eventPublisher.publishEvent(
        new NewReplyCreated(saved.getCommentId(), commentInfo.authorId(), saved.getContent(), commentInfo.postId()));

    return savedDto;
  }

  private CommentInfo getCommentInfo(Long commentId) {
    return commentRepository
        .findById(commentId)
        .map(comment -> new CommentInfo(comment.getAuthorId(), comment.getPostId()))
        .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
  }

  private record CommentInfo(Long authorId, Long postId) {}

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
