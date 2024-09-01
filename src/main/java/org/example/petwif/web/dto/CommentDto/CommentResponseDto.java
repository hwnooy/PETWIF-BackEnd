package org.example.petwif.web.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.entity.Comment;
import org.example.petwif.domain.entity.CommentImage;

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
    private List<String> imageUrl;
    private boolean isLiked;

  
    private List<CommentResponseDto> childComments; // 대댓글 목록


    @Builder
    public CommentResponseDto(Comment comment, boolean isLiked) {
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.id = comment.getId();
        this.content = comment.getContent();
        this.name = comment.getMember().getName();
        this.imageUrl=comment.getCommentImages().stream()
                .map(CommentImage::getPictureUrl)
                .collect(Collectors.toList());
        this.isLiked = isLiked;

        this.childComments = comment.getChildComments().stream()
                .map(c -> new CommentResponseDto(c,false))
                .collect(Collectors.toList()); // 대댓글 리스트를 재귀적으로 처리
    }
}