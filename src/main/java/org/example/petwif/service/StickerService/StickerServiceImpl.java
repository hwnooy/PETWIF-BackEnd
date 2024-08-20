package org.example.petwif.service.StickerService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.S3.Uuid;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.config.AmazonConfig;
import org.example.petwif.domain.entity.AlbumImage;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.MemberSticker;
import org.example.petwif.domain.entity.Sticker;
import org.example.petwif.domain.enums.StickerType;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.MemberStickerRepository;
import org.example.petwif.repository.StickerRepository;
import org.example.petwif.repository.UuidRepository;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.web.dto.StickerDto.StickerResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StickerServiceImpl implements StickerService{

    private final StickerRepository stickerRepository;
    private final MemberStickerRepository memberStickerRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final AmazonConfig amazonConfig;
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


/*
    @Transactional
    @Override
    void putStickerInDB(MultipartFile stickerImage, AlbumRequestDto.StickerRequestDto requestDto, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        // s3에 이미지 업로드
        String stickerImageUrl = s3Manager.uploadFile(s3Manager.generateStickerKeyName(savedUuid),stickerImage);

        //커버 이미지 엔티티 생성 및 저장
        Sticker savedSticker = Sticker.builder()
                .stickerName(requestDto.getStickerName())
                .stickerType(requestDto.getStickerType())
                .stickerUrl(stickerImageUrl)
                .build();
        //레포지토리에 추가해주고
        stickerRepository.save(savedSticker);


    }*/
}
