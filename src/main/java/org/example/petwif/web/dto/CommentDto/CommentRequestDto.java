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
}
