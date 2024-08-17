package org.example.petwif.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.service.CommentService.CommentReportService;
import org.example.petwif.service.CommentService.CommentService;
import org.example.petwif.service.CommentService.CommentServiceImpl;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.web.dto.CommentDto.CommentReportRequestDto;
import org.example.petwif.web.dto.CommentDto.CommentRequestDto;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
//@RequestMapping("//comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentReportService commentReportService;
    private final MemberService memberService;

    // 댓글 작성
    @PostMapping(value = "/albums/{albumId}/commment",consumes = "multipart/form-data")
    @Operation(summary = "댓글 작성 API")
    public ApiResponse<Long> writeComment(
            @PathVariable Long albumId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) Long parentCommentId,
            @RequestPart("content") String content,
            @RequestPart(value = "commentPicture", required = false) MultipartFile commentPicture) {
        try {

            Member member = memberService.getMemberByToken(authorizationHeader);
            Long memberId = member.getId();

            CommentRequestDto commentRequestDto = new CommentRequestDto();
            commentRequestDto.setContent(content);
            commentRequestDto.setCommentPicture(commentPicture);

            Long commentId = commentService.writeComment(commentRequestDto, albumId, memberId, parentCommentId);
            return ApiResponse.onSuccess(commentId);
        } catch (GeneralException e) {
            return ApiResponse.onFailure(e.getErrorReason().getHttpStatus().toString(), e.getErrorReason().getMessage(), null);
        }
    }

    // 특정 앨범에 대한 댓글 목록 조회
    @GetMapping("/albums/{albumId}/commment")
    @Operation(summary = "특정 앨범에 대한 댓글 목록 조회 API")
    public ApiResponse<List<CommentResponseDto>> getCommentsByAlbum(@PathVariable Long albumId) {
        try {
            List<CommentResponseDto> comments = commentService.commentList(albumId);
            return ApiResponse.onSuccess(comments);
        } catch (GeneralException e) {
            return ApiResponse.onFailure(e.getErrorReason().getHttpStatus().toString(), e.getErrorReason().getMessage(), null);
        }
    }

    // 댓글 수정
    @PutMapping("/comment/{commentId}")
    @Operation(summary = "댓글 수정 API")
    public ApiResponse<String> updateComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto) {
        try {
            Member member = memberService.getMemberByToken(authorizationHeader);
            Long memberId = member.getId();

            commentService.updateComment(commentRequestDto, commentId);
            return ApiResponse.onSuccess("ok");
        } catch (GeneralException e) {
            return ApiResponse.onFailure(e.getErrorReason().getHttpStatus().toString(), e.getErrorReason().getMessage(), null);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    @Operation(summary = "댓글 삭제 API")
    public ApiResponse<String> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ApiResponse.onSuccess("ok");
        } catch (GeneralException e) {
            return ApiResponse.onFailure(e.getErrorReason().getHttpStatus().toString(), e.getErrorReason().getMessage(), null);
        }
    }

    // 댓글 좋아요 추가
    @PostMapping("/comment/{commentId}/like")
    @Operation(summary = "댓글 좋아요 추가 API")
    public ApiResponse<String> likeComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long commentId) {
        try {
            Member member = memberService.getMemberByToken(authorizationHeader);
            Long memberId = member.getId();

            commentService.likeComment(commentId, memberId);
            return ApiResponse.onSuccess("ok");
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure("400", "Invalid request", null);
        } catch (GeneralException e) {
            return ApiResponse.onFailure(e.getErrorReason().getHttpStatus().toString(), e.getErrorReason().getMessage(), null);
        }
    }

    // 댓글 좋아요 제거
    @DeleteMapping("/comment/{commentId}/like")
    @Operation(summary = "댓글 좋아요 제거 API")
    public ApiResponse<String> unlikeComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long commentId)  {
        try {
            Member member = memberService.getMemberByToken(authorizationHeader);
            Long memberId = member.getId();

            commentService.unlikeComment(commentId, memberId);
            return ApiResponse.onSuccess("ok");
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure("400", "Invalid request", null);
        } catch (GeneralException e) {
            return ApiResponse.onFailure(e.getErrorReason().getHttpStatus().toString(), e.getErrorReason().getMessage(), null);
        }
    }

    //댓글 신고
    @PostMapping("/comment/{commentId}/report")
    @Operation(summary = "댓글 신고 API")
    public ApiResponse<Long> reportComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CommentReportRequestDto commentReportRequestDto,
            @PathVariable Long commentId)  {
        try {
            Member member = memberService.getMemberByToken(authorizationHeader);
            Long memberId = member.getId();

            Long reportId = commentReportService.ReportComment(commentReportRequestDto, commentId, memberId);
            return ApiResponse.onSuccess(reportId);
        } catch (GeneralException e) {
            return ApiResponse.onFailure(e.getErrorReason().getHttpStatus().toString(), e.getErrorReason().getMessage(), null);
        }
    }

}
