package org.example.petwif.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.S3.Uuid;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.config.AmazonConfig;
import org.example.petwif.domain.entity.*;
import org.example.petwif.repository.*;
import org.example.petwif.web.dto.CommentDto.CommentRequestDto;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final CommentImageRepository commentImageRepository;
    private final AmazonConfig amazonConfig;


    @Override
    // 댓글 또는 대댓글 작성 메서드
    public Long writeComment(CommentRequestDto commentRequestDto, Long albumId, Long memberId, Long parentCommentId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        Comment comment;

        if (parentCommentId == null) {
            // 일반 댓글 작성
            comment = Comment.builder()
                    .content(commentRequestDto.getContent())
                    .album(album)
                    .member(member)
                    .likeCount(0)  // 초기 좋아요 수 0으로 설정
                    .build();
        } else {
            // 대댓글 작성
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

            // 이미 대댓글인지 확인
            if (parentComment.getParentComment() != null) {
                throw new GeneralException(ErrorStatus._BAD_REQUEST);
            }

            comment = Comment.builder()
                    .content(commentRequestDto.getContent())
                    .album(album)
                    .member(member)
                    .parentComment(parentComment)
                    .likeCount(0)  // 초기 좋아요 수 0으로 설정
                    .build();

            parentComment.addChildComment(comment);
        }


        commentRepository.save(comment);

        // 댓글 이미지 처리
        if (commentRequestDto.getCommentPicture() != null && !commentRequestDto.getCommentPicture().isEmpty()) {
            // UUID 생성 및 저장
            String uuid = UUID.randomUUID().toString();
            Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

            // S3에 이미지 업로드
            String pictureUrl = s3Manager.uploadFile(
                    s3Manager.generateCommentKeyName(savedUuid),
                    commentRequestDto.getCommentPicture()
            );

            // CommentImage 엔티티 생성 및 저장
            CommentImage commentImage = CommentImage.builder()
                    .pictureUrl(pictureUrl)
                    .comment(comment)
                    .build();
            commentImageRepository.save(commentImage);

            // 댓글과 이미지 연결
            comment.addImage(commentImage);
        }

        return comment.getId();
    }


    @Override
    public List<CommentResponseDto> commentList(Long id){
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        List<Comment> commentList=commentRepository.findByAlbum(album);
        return commentList.stream()
                .map(comment-> CommentResponseDto.builder()
                        .comment(comment)
                        .build())
                .collect(Collectors.toList());
    }
    @Override
    public void updateComment(CommentRequestDto.UpdateDto commentUpdateRequestDto, Long CommentId){
        Comment comment = commentRepository.findById(CommentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));
        comment.update(commentUpdateRequestDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId){
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 댓글에 연결된 이미지들 삭제
        for (CommentImage commentImage : comment.getCommentImages()) {
            String fileUrl = commentImage.getPictureUrl();
            String keyName = fileUrl.substring(fileUrl.indexOf(amazonConfig.getCommentPath())); // 전체 경로를 포함한 keyName 추출
            s3Manager.deleteFile(keyName);
            commentImageRepository.delete(commentImage);
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    public CommentLike likeComment(Long commentId, Long memberId) {
        // 댓글이 존재하는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        // 멤버가 존재하는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        // 이미 좋아요를 누른 경우 예외 처리
        Optional<CommentLike> existingLike = commentLikeRepository.findByCommentIdAndMemberId(commentId, memberId);
        if (existingLike.isPresent()) {
            throw new IllegalArgumentException("You have already liked this comment.");
        }

        // 좋아요 추가 및 좋아요 수 증가
        CommentLike commentLike = new CommentLike(comment, member);
        comment.incrementLikeCount();  // 좋아요 수 증가
        commentRepository.save(comment);  // 변경된 likeCount 저장
        return commentLikeRepository.save(commentLike);
    }
    public void unlikeComment(Long commentId, Long memberId) {
        // 좋아요 찾기
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("Like not found"));
        // 좋아요 삭제 및 좋아요 수 감소
        Comment comment = commentLike.getComment();
        comment.decrementLikeCount();  // 좋아요 수 감소
        commentRepository.save(comment);  // 변경된 likeCount 저장
        commentLikeRepository.delete(commentLike);
    }
}