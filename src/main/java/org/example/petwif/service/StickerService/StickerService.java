package org.example.petwif.service.StickerService;

import org.example.petwif.web.dto.StickerDto.StickerResponseDto;

public interface StickerService {

    public void assignStickersToNewMember(Long memberId);
    public StickerResponseDto.StickerResultListDto getAvailableStickers(Long memberId);
}
