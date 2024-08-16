package org.example.petwif.web.dto.CommentDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
    private String content;
    private String name;
    private List<CommentResponseDto> childComments; // 대댓글 목록

    @Builder
    public CommentResponseDto(Comment comment) {
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.id = comment.getId();
        this.content = comment.getContent();
        this.name = comment.getMember().getName();
        this.childComments = comment.getChildComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()); // 대댓글 리스트를 재귀적으로 처리
    }
}
