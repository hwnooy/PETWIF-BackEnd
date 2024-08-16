package org.example.petwif.service.StickerService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.MemberSticker;
import org.example.petwif.domain.entity.Sticker;
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
public class StickerServiceImpl implements StickerService{

    private final StickerRepository stickerRepository;
    private final MemberStickerRepository memberStickerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
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

    @Override
    public StickerResponseDto.StickerResultListDto getAvailableStickers(Long memberId) {
        List<StickerResponseDto.StickerResultDto> stickerResultDtos = memberStickerRepository.findByMemberId(memberId).stream()
                .map(this::convertToStickerResultDto)
                .collect(Collectors.toList());

        return new StickerResponseDto.StickerResultListDto(stickerResultDtos);
    }

    // Entity를 DTO로 변환
    private StickerResponseDto.StickerResultDto convertToStickerResultDto(MemberSticker memberSticker) {
        return StickerResponseDto.StickerResultDto.builder()
                .id(memberSticker.getSticker().getId())
                .stickerName(memberSticker.getSticker().getStickerName())
                .stickerImageUrl(memberSticker.getSticker().getStickerUrl())
                .stickerType(memberSticker.getSticker().getStickerType())
                .build();
    }
}
