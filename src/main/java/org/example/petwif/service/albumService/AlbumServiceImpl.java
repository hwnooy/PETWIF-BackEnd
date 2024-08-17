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
    private final AmazonConfig amazonConfig;

    //앨범 생성
    @Override
    @Transactional
    public Album saveAlbum(AlbumRequestDto.SaveRequestDto requestDto, Long memberId, MultipartFile coverImage, MultipartFile[] albumImages){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //앨범 이미지 말고 다른거 toAlbumEntity로 받고
        Album album = AlbumConverter.toAlbumEntity(requestDto, member);
       // albumRepository.save(album);

        //====앨범 표지!====//
        //uuid 생성 및 저장
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        // s3에 이미지 업로드
        String coverImageUrl = s3Manager.uploadFile(s3Manager.generateAlbumKeyName(savedUuid),coverImage);

        //커버 이미지 엔티티 생성 및 저장
        AlbumImage albumCoverImage = AlbumImage.builder()
                .album(album)
                .imageURL(coverImageUrl)
                .build();
        //레포지토리에 추가해주고
        albumImageRepository.save(albumCoverImage);
        //엔티티에 추가해주고
        album.addCoverImageToAlbum(albumCoverImage);


        //==== 앨범 표지 외 사진들!! ====//
        for (MultipartFile image : albumImages) {
            //uuid 생성 및 저장
            String imageUuid = UUID.randomUUID().toString();
            Uuid imageSavedUuid = uuidRepository.save(Uuid.builder().uuid(imageUuid).build());
            // s3에 이미지 업로드
            String imageUrl = s3Manager.uploadFile(s3Manager.generateAlbumKeyName(imageSavedUuid), image);
            //커버 이미지 엔티티 생성 및 저장
            AlbumImage albumImage = AlbumImage.builder()
                    .album(album)
                    .imageURL(imageUrl)
                    .build();
            //레포지토리에 추가해주고
            albumImageRepository.save(albumImage);
            //엔티티에 추가해주고
            album.addAlbumImageToAlbum(albumImage);
        }

        //드디어 세이브,,,,,
        album = albumRepository.save(album);
        return album;
    }


    //앨범 수정
    @Override
    @Transactional
    public Album updateAlbum(Long albumId, Long memberId, AlbumRequestDto.UpdateRequestDto requestDto, MultipartFile coverImage, MultipartFile[] albumImages) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (!album.getMember().getId().equals(member.getId())) {
            throw new GeneralException(ErrorStatus.ALBUM_UNAUTHORIZED);
        }

       /* //앨범 수정의 경우, 덮어 쓰는건지, 새로 저장해도 되는건지 잘 몰라서 프론트분들꼐서 앨범 제작을 어떻게 구현했는지 보고 짜게습니다.
       // 현재는 앨범 제목, 내용, 범위만 수정 가능한 상태입니다.
        //1. 먼저 기존의 모든 앨범들을 삭제하고 다시 생성하는 메서드 입니다. 일단 주석 처리 하겠습니다.
        //기존 앨범 표지 삭제
        s3Manager.deleteFile(album.getCoverImage().getImageURL());
        albumImageRepository.delete(album.getCoverImage());
        album.setCoverImage(null);

        //기존 앨범 내부 이미지들 삭제
        album.getAlbumImages().forEach(image -> {
            s3Manager.deleteFile(image.getImageURL());
            albumImageRepository.delete(image);
        });
        album.getAlbumImages().clear();


        //2. 그냥 덮어쓸때. setter를 고민했으나, 시간이 얼마 없어서 이런식으로 구현했습니다.

        //새로운 앨범 표지 생성

        String coverImageUuid = UUID.randomUUID().toString();
        Uuid savedcoverUuid = uuidRepository.save(Uuid.builder().uuid(coverImageUuid).build());
        String coverImageUrl = s3Manager.uploadFile(s3Manager.generateAlbumKeyName(savedcoverUuid), coverImage);
        AlbumImage newAlbumCoverImage = AlbumImage.builder()
                .album(album)
                .imageURL(coverImageUrl)
                .build();
        albumImageRepository.save(newAlbumCoverImage);
        album.setCoverImage(newAlbumCoverImage);

        // 새로운 앨범 이미지들 생성
        List<AlbumImage> newImages = new ArrayList<>();
        for (MultipartFile imageFile : albumImages) {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(imageUuid).build());
                String imageUrl = s3Manager.uploadFile(s3Manager.generateAlbumKeyName(savedUuid), imageFile);
                AlbumImage newImage = AlbumImage.builder()
                        .album(album)
                        .imageURL(imageUrl)
                        .build();
                albumImageRepository.save(newImage);
                newImages.add(newImage);
            }
        }
        album.setAlbumImages(newImages);*/

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
