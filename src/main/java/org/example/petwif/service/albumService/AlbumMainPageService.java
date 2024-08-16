package org.example.petwif.service.albumService;

import org.example.petwif.domain.entity.Album;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;

import java.util.List;

public interface AlbumMainPageService {
    public List<Album> getFriendAlbums(Long memberId);
    public List<Album> getPublicAlbums();

    public AlbumResponseDto.StoryAlbumListDto convertToStoryAlbumListDto(List<Album> albums);

    public AlbumResponseDto.MainPageAlbumListDto convertToMainPageAlbumListDto(List<Album> albums);
}
