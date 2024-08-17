package org.example.petwif.service.albumService;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.AlbumLike;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.AlbumRepository;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.albumRepository.AlbumLikeRepository;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumLikeServiceImpl implements AlbumLikeService{
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;
    private final AlbumLikeRepository albumLikeRepository;
    private final AlbumCheckAccessService albumCheckAccessService;



    //좋아요 누르기
    @Transactional
    @Override
    public void addAlbumLike(Long albumId, Long memberId){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        albumCheckAccessService.checkAccess(album, memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //좋아요 중복 방지 => 손봐야함
        albumLikeRepository.findByAlbumAndMember(album, member).ifPresent(report -> {
            throw new GeneralException(ErrorStatus.ALBUM_LIKE_EXIST);
        });


        AlbumLike albumLike = AlbumLike.builder()
                .album(album)
                .member(member)
                .build();
        albumLikeRepository.save(albumLike);
    }

    //좋아요 삭제
    @Transactional
    @Override
    public void deleteAlbumLike(Long albumId, Long memberId){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        albumCheckAccessService.checkAccess(album, memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        AlbumLike albumLike = albumLikeRepository.findByAlbumAndMember(album, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_LIKE_NOT_FOUND));

       albumLikeRepository.delete(albumLike);
    }

    //좋아요 목록 보이기
    @Override
    public List<AlbumResponseDto.LikeResultDto> getAlbumLikes(Long albumId, Long memberId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        albumCheckAccessService.checkAccess(album, memberId);

        List<AlbumLike> likes = albumLikeRepository.findByAlbum(album);
        if(likes.isEmpty())throw new GeneralException(ErrorStatus.ALBUM_LIKE_PAGE_NOT_FOUND);
        return likes.stream()
                .map(like -> new AlbumResponseDto.LikeResultDto(like.getMember().getId(), like.getMember().getNickname(), like.getMember().getProfile_url()))
                .collect(Collectors.toList());
    }


    //좋아요 수 세기
    public int countAlbumLikes(Long albumId){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        return albumLikeRepository.countByAlbum(album);
    }
}
