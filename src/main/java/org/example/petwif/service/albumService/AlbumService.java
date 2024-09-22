package org.example.petwif.service.albumService;

import org.example.petwif.domain.entity.Album;
import org.example.petwif.web.dto.albumDto.AlbumRequestDto;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface AlbumService {
    Album saveAlbum(AlbumRequestDto.SaveRequestDto requestDto, Long memberId, MultipartFile coverImage, MultipartFile albumImages);
    Album updateAlbum(Long albumId, Long memberId, AlbumRequestDto.UpdateRequestDto requestDto);//, MultipartFile coverImage, MultipartFile[] albumImages);
    void deleteAlbum(Long albumId, Long memberId);
    void increaseView(Long albumId, Long memberId);
}