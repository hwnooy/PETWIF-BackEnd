package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="album_id")
    private Album album;

    private String imageURL;

    public static class AlbumImageBuilder {
        public AlbumImageBuilder album(Album album) {
            this.album = album;
            return this;
        }

        public AlbumImageBuilder imageUrl(String imageUrl) {
            this.imageURL = imageUrl;
            return this;
        }
    }

   //앨범 이미지 content 삭제
}


