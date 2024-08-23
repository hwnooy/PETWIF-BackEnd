
package org.example.petwif.service.StickerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.S3.Uuid;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.config.AmazonConfig;
import org.example.petwif.domain.entity.AlbumImage;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Sticker;
import org.example.petwif.domain.enums.StickerType;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.StickerRepository;
import org.example.petwif.service.stickerService.StickerService;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StickerServiceImpl implements StickerService {

    private final StickerRepository stickerRepository;
    @Override
    public StickerResponseDto.StickerResultListDto getMyStickers(){
//        Sticker sticker1 = new Sticker();
//        Sticker sticker4= new Sticker();
//        Sticker sticker6 = new Sticker();
//        Sticker sticker7 = new Sticker();
//        Sticker sticker11 = new Sticker();
//        Sticker sticker12 = new Sticker();
        Sticker sticker1 = stickerRepository.findById(1L);
        Sticker sticker4 = stickerRepository.findById(4L);
        Sticker sticker6 = stickerRepository.findById(6L);
        Sticker sticker7 = stickerRepository.findById(7L);
        Sticker sticker11 = stickerRepository.findById(11L);
        Sticker sticker12 = stickerRepository.findById(12L);

        List<Sticker> stickers = new ArrayList<>();
        stickers.add(sticker1);
        stickers.add(sticker4);
        stickers.add(sticker6);
        stickers.add(sticker7);
        stickers.add(sticker11);
        stickers.add(sticker12);
        List<StickerResponseDto.StickerResultDto> stickerResultDtos = stickers.stream()
                .map(this::convertToStickerResultDto)
                .collect(Collectors.toList());

        return new StickerResponseDto.StickerResultListDto(stickerResultDtos);
    }


    @Override
    public StickerResponseDto.StickerResultListDto getMarketStickers() {
        Sticker sticker2 = stickerRepository.findById(2L);
        Sticker sticker3 = stickerRepository.findById(3L);
        Sticker sticker5 = stickerRepository.findById(5L);
        Sticker sticker8 = stickerRepository.findById(8L);
        Sticker sticker9 = stickerRepository.findById(9L);
        Sticker sticker10 = stickerRepository.findById(10L);

        List<Sticker> stickers = new ArrayList<>();
        stickers.add(sticker2);
        stickers.add(sticker3);
        stickers.add(sticker5);
        stickers.add(sticker8);
        stickers.add(sticker9);
        stickers.add(sticker10);
        List<StickerResponseDto.StickerResultDto> stickerResultDtos = stickers.stream()
                .map(this::convertToStickerResultDto)
                .collect(Collectors.toList());

        return new StickerResponseDto.StickerResultListDto(stickerResultDtos);
    }



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

