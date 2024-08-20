package org.example.petwif.service.albumService;


import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlbumBookmarkService {
    public void addBookmark(Long albumId, Long memberId);
    public void deleteBookmark(Long albumId, Long memberId);
    public Slice<AlbumResponseDto.BookmarkResultDto> getAlbumBookmarks(Long albumId, Long memberId, Integer page);
}

