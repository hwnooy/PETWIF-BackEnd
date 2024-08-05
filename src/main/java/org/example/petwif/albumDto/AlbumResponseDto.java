package org.example.petwif.albumDto;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class AlbumResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long memberId;

    public AlbumResponseDto(Long id, String title, String content, Long memberId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}
