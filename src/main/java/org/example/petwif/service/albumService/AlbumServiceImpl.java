package org.example.petwif.service.albumService;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.petwif.S3.AmazonS3Controller;
import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.S3.Uuid;
import org.example.petwif.albumConverter.AlbumConverter;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.apiPayload.exception.handler.AlbumHandler;
import org.example.petwif.apiPayload.exception.handler.BlockHandler;
import org.example.petwif.config.AmazonConfig;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.AlbumBookmark;
import org.example.petwif.domain.entity.AlbumImage;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.AlbumRepository;
import org.example.petwif.repository.CommentImageRepository;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.UuidRepository;
import org.example.petwif.repository.albumRepository.AlbumImageRepository;
import org.example.petwif.web.dto.albumDto.AlbumRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;
    private final AlbumImageRepository albumImageRepository;

    //S3 추가
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final CommentImageRepository commentImageRepository;
    private final AmazonConfig amazonConfig;

    //앨범 생성
    @Override
    @Transactional
    public Album saveAlbum(AlbumRequestDto.SaveRequestDto requestDto, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //앨범 이미지 말고 다른거 toAlbumEntity로 받고
        Album album = AlbumConverter.toAlbumEntity(requestDto,member);
       // albumRepository.save(album);

        //앨범 표지!
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        String coverImageUrl = s3Manager.uploadFile(s3Manager.generateAlbumKeyName(savedUuid), requestDto.getCoverImage());

        AlbumImage coverImage = AlbumImage.builder()
                .album(album)
                .imageURL(coverImageUrl)
                .build();
        //레포지토리에 추가해주고
        albumImageRepository.save(coverImage);
        //엔티티에 추가해주고
        album.addCoverImageToAlbum(coverImage);


        // 앨범 표지 외 사진들!!
        // 밑에 메서드 작성함
        Album copiedAlbum = album;
        List<String> albumImageUrls = uploadAlbumImages(requestDto.getAlbumImages());
        List<AlbumImage> albumImages = albumImageUrls.stream()
                .map(url -> AlbumImage.builder()
                        .album(copiedAlbum)
                        .imageUrl(url)
                        .build())
                .collect(Collectors.toList());

        //레포지토리에 추가해주고
        albumImageRepository.saveAll(albumImages);
        //엔티티에 추가해주고
        album.addAlbumImagesToAlbum(albumImages);


        //드디어 세이브,,,,,
        album = albumRepository.save(album);
        return album;
    }

    private List<String> uploadAlbumImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String uuid = UUID.randomUUID().toString();
            Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
            String albumImageKey = s3Manager.generateAlbumKeyName(savedUuid);
            String imageUrl = s3Manager.uploadFile(albumImageKey, image);
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    //앨범 수정
    @Override
    @Transactional
    public Album updateAlbum(Long albumId, Long memberId, AlbumRequestDto.UpdateRequestDto requestDto){
        Album album =  albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if(!album.getMember().getId().equals(member.getId())){
            throw new GeneralException(ErrorStatus.ALBUM_UNAUTHORIZED);
        }
        album.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getScope());
        return albumRepository.save(album);

    }


    //앨범 삭제
    @Override
    @Transactional
    public void deleteAlbum(Long albumId, Long memberId){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if(!album.getMember().getId().equals(member.getId())){
            throw new GeneralException(ErrorStatus.ALBUM_UNAUTHORIZED);
        }
        albumRepository.delete(album);
    }

    @Override
    @Transactional
    public void increaseView(Long albumId, Long memberId){
        Album album =  albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (!album.getMember().getId().equals(member.getId())) {
        album.incrementViewCount();
        albumRepository.save(album);
    }

    }

}
