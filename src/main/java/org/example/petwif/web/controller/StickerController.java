/*
package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.service.StickerService.StickerService;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@Validated
public class StickerController {

    @Autowired
    StickerService stickerService;


    //앨범 생성시, 스티커 눌를때
    @GetMapping("/albums/sticker/my")
    private ApiResponse<StickerResponseDto.StickerResultListDto> getMyStickerForAlbumSave(){

        log.info("StickerService injected: {}", stickerService != null); //
        StickerResponseDto.StickerResultListDto stickers = stickerService.getMyStickers();
        return ApiResponse.onSuccess(stickers);
    }

}
*/
