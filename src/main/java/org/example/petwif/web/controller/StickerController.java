package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.domain.entity.Sticker;
import org.example.petwif.service.StickerService.StickerServiceImpl;
import org.example.petwif.service.stickerService.StickerService;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class StickerController {

  private final StickerServiceImpl stickerService;
  @GetMapping("/albums/sticker/my")
    public ApiResponse<StickerResponseDto.StickerResultListDto> getMyStickerForAlbumSave(){
      log.info("StickerService injected: {}", stickerService != null);
      StickerResponseDto.StickerResultListDto stickers = stickerService.getMyStickers();
      return ApiResponse.onSuccess(stickers);
  }

   @GetMapping("/albums/sticker/market")
    public ApiResponse<StickerResponseDto.StickerResultListDto> getMarketStickerForAlbumSave(){
        log.info("StickerService injected: {}", stickerService != null);
        StickerResponseDto.StickerResultListDto stickers = stickerService.getMarketStickers();
        return ApiResponse.onSuccess(stickers);
    }
}
