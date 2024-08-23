package org.example.petwif.service.stickerService;

import org.example.petwif.web.dto.StickerDto.StickerResponseDto;

public interface StickerService {

    //public void assignStickersToNewMember(Long memberId);
    public StickerResponseDto.StickerResultListDto getMyStickers();

    public StickerResponseDto.StickerResultListDto getMarketStickers();



    //public void assignStickersToNewMember(Long memberId);


    //void putStickerInDB(MultipartFile stickerImage, AlbumRequestDto.StickerRequestDto requestDto, Long memberId);
}