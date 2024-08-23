package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.domain.entity.Sticker;
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


    //앨범 생성시, 스티커 눌를때
    @GetMapping("/albums/sticker/my")
    private ApiResponse<StickerResponseDto.StickerResultListDto> getMyStickerForAlbumSave(){
        StickerResponseDto.StickerResultListDto stickers = stickerService.getMyStickers();
        return ApiResponse.onSuccess(stickers);
    }

    @GetMapping("/albums/sticker/market")
    private ApiResponse<StickerResponseDto.StickerResultListDto> getMarketStickerForAlbumSave(){
        StickerResponseDto.StickerResultListDto stickers = stickerService.getMarketStickers();
        return ApiResponse.onSuccess(stickers);
    }

}
