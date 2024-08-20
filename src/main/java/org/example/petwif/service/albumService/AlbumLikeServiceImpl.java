package org.example.petwif.service.albumService;


import jakarta.persistence.EntityManager;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
    private final EntityManager em;


    private final AlbumCheckAccessService albumCheckAccessService;



    //좋아요 누르기
    @Transactional
    @Override
    public void addAlbumLike(Long albumId, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        albumCheckAccessService.checkAccess(album, memberId);

        if(albumLikeRepository.existsByAlbumAndMemberId(album, memberId))throw new GeneralException(ErrorStatus.ALBUM_LIKE_EXIST);

        AlbumLike albumLike = AlbumLike.builder()
                .album(album)
                .member(member)
                .build();
        albumLikeRepository.saveAndFlush(albumLike);
        em.flush();
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
        AlbumLike albumLike = albumLikeRepository.findByAlbumIdAndMemberId(albumId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_LIKE_NOT_FOUND));

       albumLikeRepository.delete(albumLike);
    }

    //좋아요 목록 보이기
    @Override
    public Slice<AlbumLike> getAlbumLikes(Long albumId, Long memberId, Integer page) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        albumCheckAccessService.checkAccess(album, memberId);

        Slice<AlbumLike> likes = albumLikeRepository.findByAlbum(album, PageRequest.of(page, 10));
        if(likes.isEmpty())throw new GeneralException(ErrorStatus.ALBUM_LIKE_PAGE_NOT_FOUND);
        return likes;
    }


}
