package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.MemberSticker;
import org.example.petwif.domain.enums.StickerType;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.service.StickerService.StickerService;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
//@CrossOrigin(origins = "*", allowedHeaders = "*")
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

   /* @PostMapping("/albums/admin/sticker")
    private ApiResponse<Void> putStickerInDB(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestPart(value = "stickerImage", required = true) MultipartFile stickerImage,
                                            @RequestPart(value = "requestDto") AlbumRequestDto.StickerRequestDto requestDto){
        Member member = memberService.getMemberByToken(authorizationHeader);
        stickerService.putStickerInDB(stickerImage, requestDto, member.getId());
        return ApiResponse.onSuccess(null);
    }*/
}
