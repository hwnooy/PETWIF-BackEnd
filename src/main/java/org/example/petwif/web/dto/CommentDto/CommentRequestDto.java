package org.example.petwif.web.dto.CommentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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