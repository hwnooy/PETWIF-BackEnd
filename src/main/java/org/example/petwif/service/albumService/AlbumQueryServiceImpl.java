package org.example.petwif.service.albumService;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.entity.*;
import org.example.petwif.domain.enums.FriendStatus;
import org.example.petwif.domain.enums.Scope;
import org.example.petwif.repository.*;
import org.example.petwif.repository.albumRepository.AlbumBookmarkRepository;
import org.example.petwif.repository.albumRepository.AlbumLikeRepository;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumQueryServiceImpl implements AlbumQueryService{

    private final AlbumRepository albumRepository;
    private final BlockRepository blockRepository;
    private final FriendRepository friendRepository;
    private final CommentRepository commentRepository;
    private final AlbumLikeRepository albumLikeRepository;
    private final AlbumBookmarkRepository albumBookmarkRepository;


    //열람 조회 가능한지 메서드
    private final AlbumCheckAccessService albumCheckAccessService;

    @Override
    public Optional<Album> findAlbum(Long albumId){
        return albumRepository.findById(albumId);
    }

    //================================= 1. 앨범 조회 -> 앨범 세부 페이지====================================//

   @Override
    public AlbumResponseDto.DetailResultDto getAlbumDetails(Long albumId, Long memberId){
        Album album =  albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        //차단, 친구인지 확인
       albumCheckAccessService.checkAccess(album, memberId);


       List<Comment> comments = commentRepository.findByAlbum(album); //댓글 리스트
       return AlbumResponseDto.DetailResultDto.builder()
               .albumId(album.getId())
               .title(album.getTitle())
               .content(album.getContent())
               .scope(album.getScope())
               .createdAt(album.getCreatedAt())
               .updatedAt(album.getUpdatedAt())
               .coverImage(album.getCoverImage())
               .albumImages(new ArrayList<>(album.getAlbumImages()))
               .viewCount(album.getView())
               .likeCount(albumLikeRepository.countByAlbum(album))
               .everyCommentCount(comments.size())
               .bookmarkCount(albumBookmarkRepository.countByAlbum(album))
               .comments(comments.stream()
                       .map(c -> new CommentResponseDto(c))
                       .collect(Collectors.toList()))
               .build();
       }

    //===============================2. 앨범 조회 -> 메인 페이지 (스토리형식 + 게시글형식)================================//
    // 2. 앨범 조회 -> 메인 페이지 (스토리형식 + 게시글형식) 일단 해볼게요...어려웡
    /*@Override
    public AlbumResponseDto.MainPageContentDto getMainPageContent(Long memberId) {
        List<Album> friendAlbums = albumMainPageService.getFriendAlbums(memberId);
        List<Album> publicAlbums = albumMainPageService.getPublicAlbums();

        AlbumResponseDto.StoryAlbumListDto stories = albumMainPageService.convertToStoryAlbumListDto(friendAlbums);
        AlbumResponseDto.MainPageAlbumListDto mainpageAlbums = albumMainPageService.convertToMainPageAlbumListDto(publicAlbums);

        return AlbumResponseDto.MainPageContentDto.builder()
                .storyAlbumListDto(stories)
                .mainpageAlbumListDto(mainpageAlbums)
                .build();
    }*/

    //===============================3. 탐색 페이지에서 앨범 조회 서비스================================//
    @Override
    public AlbumResponseDto.SearchAlbumListDto getSearchableAlbums(Long memberId) {
        List<Album> allAlbums = albumRepository.findAllByOrderByCreatedAtDesc();

        List<AlbumResponseDto.SearchAlbumDto> accessibleAlbums = allAlbums.stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId))
                .map(this::convertToSearchAlbumDto)
                .collect(Collectors.toList());

        if(accessibleAlbums.isEmpty()){
            throw new GeneralException(ErrorStatus.ALBUM_LIST_NOT_FOUND);
        }

        return new AlbumResponseDto.SearchAlbumListDto(accessibleAlbums);
    }

    @Builder
    private AlbumResponseDto.SearchAlbumDto convertToSearchAlbumDto(Album album) {
        return AlbumResponseDto.SearchAlbumDto.builder()
                .albumId(album.getId())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(albumLikeRepository.countByAlbum(album))
                .bookmarkCount(albumBookmarkRepository.countByAlbum(album))
                .commentCount(commentRepository.findByAlbum(album).size())
                .build();
    }

    //===========================4. 특정 멤버의 앨범 페이지에서 앨범 조회 => 나, 다른사람 포함============================//

    //=================================== 5. 북마크한 앨범 에서 앨범 조회====================================//
    public AlbumResponseDto.MemberBookmarkAlbumListDto getMemberBookmarkAlbums(Long memberId){
        List<Album> accessibleAlbums = albumRepository.findAll().stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId))
                .collect(Collectors.toList());

        // 접근 가능한 앨범 중에서 사용자가 북마크한 앨범만 필터링
        List<Long> bookmarkedAlbumIds = albumBookmarkRepository.findBookmarkedAlbumIdsByMemberId(memberId);
        List<AlbumResponseDto.MemberBookmarkAlbumDto> bookmarkedAlbums = accessibleAlbums.stream()
                .filter(album -> bookmarkedAlbumIds.contains(album.getId()))
                .map(this::convertToMemberBookmarkAlbumDto)
                .collect(Collectors.toList());


        // 조회된 앨범이 없다면 예외 발생
        if (bookmarkedAlbums.isEmpty()) {
            throw new GeneralException(ErrorStatus.ALBUM_LIST_NOT_FOUND);
        }

        // 결과를 MemberBookmarkAlbumListDto 객체로 래핑하여 반환
        return new AlbumResponseDto.MemberBookmarkAlbumListDto(bookmarkedAlbums);

    }

    private AlbumResponseDto.MemberBookmarkAlbumDto convertToMemberBookmarkAlbumDto(Album album) {
        return AlbumResponseDto.MemberBookmarkAlbumDto.builder()
                .albumId(album.getId())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(albumLikeRepository.countByAlbum(album))
                .bookmarkCount(albumBookmarkRepository.countByAlbum(album))
                .commentCount(commentRepository.findByAlbum(album).size())
                .build();
    }


}
