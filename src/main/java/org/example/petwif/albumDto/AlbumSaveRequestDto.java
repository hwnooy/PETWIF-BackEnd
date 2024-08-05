package org.example.petwif.albumDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.albumService.AlbumService;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Member;

@Getter
@NoArgsConstructor
public class AlbumSaveRequestDto {

    private String title;
    private String content;
    private Long member;

    @Builder
    public AlbumSaveRequestDto (String title, String content, Long member){
        this.title = title;
        this.content = content;
        this.member = member;
    }

     public Album toEntity(){
        return Album.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

}
