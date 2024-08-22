package org.example.petwif.service.albumService;

import io.swagger.models.auth.In;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.AlbumImage;
import org.example.petwif.domain.entity.AlbumLike;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface AlbumLikeService {
    public void addAlbumLike(Long albumId, Long memberId);

    public void deleteAlbumLike(Long albumId, Long memberId);

    public Slice<AlbumLike> getAlbumLikes(Long albumId, Long memberId, Integer page);
}
