package org.example.petwif.service.StickerService;

import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.config.AmazonConfig;
import org.example.petwif.domain.enums.StickerType;
import org.example.petwif.repository.UuidRepository;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface StickerService {

    public void assignStickersToNewMember(Long memberId);
    public StickerResponseDto.StickerResultListDto getAvailableStickers(Long memberId);

    //void putStickerInDB(MultipartFile stickerImage, AlbumRequestDto.StickerRequestDto requestDto, Long memberId);
}
