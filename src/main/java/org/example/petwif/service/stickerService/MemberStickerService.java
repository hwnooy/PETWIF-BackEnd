package org.example.petwif.service.stickerService;


import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.MemberSticker;
import org.example.petwif.domain.entity.Sticker;
import org.example.petwif.domain.enums.StickerType;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.MemberStickerRepository;
import org.example.petwif.repository.StickerRepository;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberStickerService {
    private final MemberRepository memberRepository;
    private final StickerRepository stickerRepository;
    private final MemberStickerRepository memberStickerRepository;
    @Transactional
    public void assignStickersToNewMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Sticker> allStickers = stickerRepository.findAll();
        List<MemberSticker> memberStickers = allStickers.stream()
                .map(sticker -> MemberSticker.builder()
                        .member(member)
                        .sticker(sticker)
                        .build())
                .collect(Collectors.toList());
        memberStickerRepository.saveAll(memberStickers);
    }

    public StickerResponseDto.StickerResultListDto getMyStickers(){
        List<MemberSticker> stickers = memberStickerRepository.findBySticker_StickerType(StickerType.FREE);
        List<StickerResponseDto.StickerResultDto> stickerResultDtos = stickers.stream()
                .map(this::convertToStickerResultDto)
                .collect(Collectors.toList());

        return new StickerResponseDto.StickerResultListDto(stickerResultDtos);
    }

    public StickerResponseDto.StickerResultListDto getMarketStickers() {
        List<MemberSticker> stickers = memberStickerRepository.findBySticker_StickerType(StickerType.FORSALE);

        List<StickerResponseDto.StickerResultDto> stickerResultDtos = stickers.stream()
                .map(this::convertToStickerResultDto)
                .collect(Collectors.toList());

        return new StickerResponseDto.StickerResultListDto(stickerResultDtos);
    }

    private StickerResponseDto.StickerResultDto convertToStickerResultDto(MemberSticker memberSticker) {
        return StickerResponseDto.StickerResultDto.builder()
                .id(memberSticker.getId())
                .stickerName(memberSticker.getSticker().getStickerName())
                .stickerImageUrl(memberSticker.getSticker().getStickerUrl())
                .stickerType(memberSticker.getSticker().getStickerType())
                .build();
    }

}
