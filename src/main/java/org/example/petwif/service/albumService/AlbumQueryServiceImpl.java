package org.example.petwif.service.albumService;

import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.*;
import org.example.petwif.domain.enums.AlbumSortType;
import org.example.petwif.domain.enums.Scope;
import org.example.petwif.repository.*;
import org.example.petwif.repository.albumRepository.AlbumBookmarkRepository;
import org.example.petwif.repository.albumRepository.AlbumLikeRepository;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.PageRanges;
import java.awt.print.Pageable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumQueryServiceImpl implements AlbumQueryService{

    private final AlbumRepository albumRepository;
    private final CommentRepository commentRepository;
    private final AlbumLikeRepository albumLikeRepository;
    private final AlbumBookmarkRepository albumBookmarkRepository;
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

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

       List<String> albumImageUrls = album.getAlbumImages().stream()
               .map(AlbumImage::getImageURL)
               .collect(Collectors.toList());;
       boolean isLiked = albumLikeRepository.existsByAlbumAndMemberId(album, memberId);
       boolean isBookmarked = albumBookmarkRepository.existsByAlbumAndMemberId(album, memberId);

       List<Comment> comments = commentRepository.findByAlbum(album); //댓글 리스트
       return AlbumResponseDto.DetailResultDto.builder()
               .albumId(album.getId())
               .title(album.getTitle())
               .content(album.getContent())
               .scope(album.getScope())
               .updatedAt(album.getUpdatedAt())
               .updatedAt(album.getUpdatedAt())
               .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
               .albumImages(albumImageUrls)
               .viewCount(album.getView())
               .likeCount(albumLikeRepository.countByAlbum(album))
               .everyCommentCount(comments.size())
               .bookmarkCount(albumBookmarkRepository.countByAlbum(album))
               .isLiked(isLiked)
               .isBookmarked(isBookmarked)
               .comments(comments.stream()
                       .map(c -> new CommentResponseDto(c))
                       .collect(Collectors.toList()))

               .build();
       }

    //=============================== 2. 앨범 조회 -> 메인 페이지 (스토리형식 + 게시글형식)================================//
    // 2-1. 앨범 조회 -> 스토리형식 조회
    @Override
    public AlbumResponseDto.StoryAlbumListDto getStoryAlbum(Long memberId, Integer page) {

        List<Member> friends = memberRepository.findFriendsByMemberId(memberId);
        List<AlbumResponseDto.StoryAlbumResultDto> stories = new ArrayList<>();

        for(Member friend : friends){
            List<Album> albums = albumRepository.findByMemberIdOrderByUpdatedAtDesc(friend.getId());
            for (Album album : albums){
                if(albumCheckAccessService.checkAccessInBool(album, memberId) && findScope(album))
                    stories.add(convertToStoryAlbumResultDto(album));

            }
        }
        return new AlbumResponseDto.StoryAlbumListDto(stories);
    }

    private boolean findScope(Album album){
        if(album.getScope().equals(Scope.FRIEND) || album.getScope().equals(Scope.ALL)){
            return true;
        }
        else return false;
    }


    private AlbumResponseDto.StoryAlbumResultDto convertToStoryAlbumResultDto(Album album){
        return AlbumResponseDto.StoryAlbumResultDto.builder()
                .albumId(album.getId())
                .nickName(album.getMember().getNickname())
                .profileImageUrl(album.getMember().getProfile_url())
                .updatedAT(album.getUpdatedAt())
                .build();
    }

    //Slice 포함해서
/*    @Override
    public Slice<Album> getStoryAlbum(Long memberId, Integer page) {
        // Pageable 객체 생성
        Pageable pageable = (Pageable) PageRequest.of(page, 10);


        // 친구 목록 조회
        List<Long> friendIds = memberRepository.findFriendsByMemberId(memberId)
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        Slice<Album> albumsSlice = albumRepository.findStoryAlbumsByMemberIds(friendIds, pageable)
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId) &&
                        album.getScope() != Scope.MY);

        return albumsSlice;
    }*/


    // 2-2. 앨범 조회 -> 게시글 형식 조회
    @Override
    public AlbumResponseDto.MainPageAlbumListDto getMainpageAlbum(Long memberId){
        List<Member> notFriends = memberRepository.findNonFriendsByMemberId(memberId);
        List<AlbumResponseDto.MainPageAlbumResultDto> posts = new ArrayList<>();


        for(Member notFriend : notFriends){
            List<Album> albums = albumRepository.findByMemberIdOrderByUpdatedAtDesc(notFriend.getId());
            for (Album album : albums){
                boolean isLiked = albumLikeRepository.existsByAlbumAndMemberId(album, memberId);
                boolean isBookmarked = albumBookmarkRepository.existsByAlbumAndMemberId(album, memberId);
                List<Comment> comments = commentRepository.findByAlbum(album);
                if(albumCheckAccessService.checkAccessInBool(album, memberId) && album.getScope().equals(Scope.ALL))
                    posts.add(convertToPostAlbumResultDto(album, comments, isLiked, isBookmarked));

            }
        }
        return new AlbumResponseDto.MainPageAlbumListDto(posts);
         //댓글 리스트
    }

    private AlbumResponseDto.MainPageAlbumResultDto convertToPostAlbumResultDto(Album album, List<Comment> comments, boolean isLiked, boolean isBookmarked){
        return AlbumResponseDto.MainPageAlbumResultDto.builder()
                .albumId(album.getId())
                .content(album.getContent())
                .updatedAt(album.getUpdatedAt())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .nickName(album.getMember().getNickname())
                .profileImageUrl(album.getMember().getProfile_url())
                .memberId(album.getMember().getId())
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .comments(comments.stream()
                        .map(c -> new CommentResponseDto(c))
                        .collect(Collectors.toList()))
                .build();
    }


    //=============================== 3. 탐색 페이지에서 앨범 조회 서비스================================//
    @Override
    public AlbumResponseDto.SearchAlbumListDto getSearchableAlbums(Long memberId) {
        List<Album> albums = albumRepository.findAllByOrderByUpdatedAtDesc();

        List<AlbumResponseDto.SearchAlbumDto> accessibleAlbums = albums.stream()
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
                .updatedAt(album.getUpdatedAt())
                .build();
    }



    //=========================== 4. 특정 멤버의 앨범 페이지에서 앨범 조회 => 나, 다른사람 포함============================//
    @Override
    public AlbumResponseDto.UserAlbumViewListDto getMemberPageAlbums(Long memberId, Long pageOwnerId, AlbumSortType sortType) {
        List<Album> albums = albumRepository.findAlbumsByMemberId(pageOwnerId);
        List<AlbumResponseDto.UserAlbumViewDto> albumDtos = albums.stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId))
                .map(this::convertToUserAlbumDto)
                .sorted(getComparator(sortType))
                .collect(Collectors.toList());

        return AlbumResponseDto.UserAlbumViewListDto.builder()
                .albums(albumDtos)
                .totalAlbumCount(albumDtos.size())
                .build();
    }

    private AlbumResponseDto.UserAlbumViewDto convertToUserAlbumDto(Album album) {
        return AlbumResponseDto.UserAlbumViewDto.builder()
                .albumId(album.getId())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .bookmarkCount(album.getAlbumBookmarks().size())
                .commentCount(album.getCommentList().size())
                .updatedAt(album.getUpdatedAt())
                .build();
    }

    private Comparator<AlbumResponseDto.UserAlbumViewDto> getComparator(AlbumSortType sortType) {
        switch (sortType) {
            case LIKES:
                return Comparator.comparingInt(AlbumResponseDto.UserAlbumViewDto::getLikeCount).reversed();
            case COMMENTS:
                return Comparator.comparingInt(AlbumResponseDto.UserAlbumViewDto::getCommentCount).reversed();
            case BOOKMARKS:
                return Comparator.comparingInt(AlbumResponseDto.UserAlbumViewDto::getBookmarkCount).reversed();
            case LATEST:
            default:
                return Comparator.comparing(AlbumResponseDto.UserAlbumViewDto::getUpdatedAt).reversed(); // 최신순은 앨범 ID를 기준으로 역순 정렬
        }
    }


    //=================================== 5. 북마크한 앨범 에서 앨범 조회 ====================================//
    @Override
    public AlbumResponseDto.MemberBookmarkAlbumListDto getMemberBookmarkAlbums(Long memberId){
        List<Album> albums = albumRepository.findAll().stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId))
                .collect(Collectors.toList());

        // 접근 가능한 앨범 중에서 사용자가 북마크한 앨범만 필터링
        List<Long> bookmarkedAlbumIds = albumBookmarkRepository.findBookmarkedAlbumIdsByMemberId(memberId);
        List<AlbumResponseDto.MemberBookmarkAlbumDto> bookmarkedAlbums = albums.stream()
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
                .updatedAt(album.getUpdatedAt())
                .build();
    }


}
