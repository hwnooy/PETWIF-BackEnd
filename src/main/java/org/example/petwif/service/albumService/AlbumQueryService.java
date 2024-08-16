package org.example.petwif.service.albumService;

import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;

import java.util.List;
import java.util.Optional;

public interface AlbumQueryService {
    Optional<Album> findAlbum(Long AlbumId);

    public AlbumResponseDto.DetailResultDto getAlbumDetails(Long albumId, Long memberId);
    //public List<AlbumResponseDto.StoryResponseDto> getFriendAlbumStories(Long memberId);


    //public AlbumResponseDto.MainPageContentDto getMainPageContent(Long memberId);

    //탐색 페이지에서 앨범 조회
    public AlbumResponseDto.SearchAlbumListDto getSearchableAlbums(Long memberId);
}
