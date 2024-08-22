package org.example.petwif.service.albumService;

import io.swagger.models.auth.In;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.enums.AlbumSortType;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface AlbumQueryService {
    Optional<Album> findAlbum(Long AlbumId);

    //앨범 세부페이지
    public AlbumResponseDto.DetailResultDto getAlbumDetails(Long albumId, Long memberId);

    //메인페이지 -> 스토리형식
    public Slice<Album> getStoryAlbum(Long memberId, Integer page);
    //메인페이지 -> 게시글형식
    public Slice<AlbumResponseDto.MainPageAlbumResultDto> getMainpageAlbum(Long memberId, Integer page);

    //탐색 페이지에서 앨범 조회
    public Slice<Album> getSearchableAlbums(Long memberId, Integer page);

    // 사용자 페이지에서 앨범 조회
    public Slice<AlbumResponseDto.UserAlbumViewDto> getMemberPageAlbums(Long pageOwnerId, Long currentUserId, Integer page, AlbumSortType sortType);

    //북마크한 앨범 조회
    public Slice<AlbumResponseDto.MemberBookmarkAlbumDto> getMemberBookmarkAlbums(Long memberId, Integer page, AlbumSortType sortType);
}
