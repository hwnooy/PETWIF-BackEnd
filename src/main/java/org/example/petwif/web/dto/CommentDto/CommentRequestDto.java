package org.example.petwif.web.dto.CommentDto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private String content;

    private MultipartFile commentPicture;

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    // 댓글 수정을 위한 내부 클래스
    public static class UpdateDto {

        private String content;
    }

}
