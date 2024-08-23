package org.example.petwif.service.stickerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.petwif.domain.entity.Sticker;
import org.example.petwif.domain.enums.StickerType;
import org.example.petwif.repository.StickerRepository;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StickerServiceImpl implements StickerService{

    private final StickerRepository stickerRepository;
    @Override
    public StickerResponseDto.StickerResultListDto getMyStickers(){
        List<Sticker> stickers = stickerRepository.findStickersByStickerType(StickerType.FREE);
        List<StickerResponseDto.StickerResultDto> stickerResultDtos = stickers.stream()
                .map(this::convertToStickerResultDto)
                .collect(Collectors.toList());


        return new StickerResponseDto.StickerResultListDto(stickerResultDtos);
    }
    /*@Override
    public StickerResponseDto.StickerResultListDto getMarketStickers(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        List<StickerResponseDto.StickerResultDto> stickerResultDtos = stickerRepository.findStickersByStickerType(StickerType.FORSALE).stream()
                .map(this::convertToStickerResultDto)
                .collect(Collectors.toList());

        return new StickerResponseDto.StickerResultListDto(stickerResultDtos);
    }*/



    // Entity를 DTO로 변환
    private StickerResponseDto.StickerResultDto convertToStickerResultDto(Sticker sticker) {
        return StickerResponseDto.StickerResultDto.builder()
                .id(sticker.getId())
                .stickerName(sticker.getStickerName())
                .stickerImageUrl(sticker.getStickerUrl())
                .stickerType(sticker.getStickerType())
                .build();
    }

}
