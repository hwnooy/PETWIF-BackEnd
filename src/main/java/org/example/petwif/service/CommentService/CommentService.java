package org.example.petwif.service.CommentService;

import org.example.petwif.domain.entity.CommentLike;
import org.example.petwif.web.dto.CommentDto.CommentRequestDto;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;

import java.util.List;
public interface CommentService {
    public Long writeComment(CommentRequestDto commentRequestDto, Long albumId, Long memberId, Long parentCommentId);
    public List<CommentResponseDto> commentList(Long id);
    public void updateComment(CommentRequestDto.UpdateDto commentUpdateRequestDto, Long CommentId);
    public void deleteComment(Long commentId);
    public CommentLike likeComment(Long commentId, Long memberId);
    public void unlikeComment(Long commentId, Long memberId);

}