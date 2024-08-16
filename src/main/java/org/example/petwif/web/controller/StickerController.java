package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.MemberSticker;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.service.StickerService.StickerService;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StickerController {
    private final StickerService stickerService;
    private final MemberService memberService;


    //앨범 생성시, 스티커 눌를때
    @GetMapping("/albums/sticker")
    private ApiResponse<StickerResponseDto.StickerResultListDto> getStickerForAlbumSave(@RequestHeader("Authorization") String authorizationHeader){
        Member member = memberService.getMemberByToken(authorizationHeader);
        StickerResponseDto.StickerResultListDto stickers = stickerService.getAvailableStickers(member.getId());
        return ApiResponse.onSuccess(stickers);
    }
}
