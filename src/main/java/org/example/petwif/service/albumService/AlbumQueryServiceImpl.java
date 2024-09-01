package org.example.petwif.service.albumService;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.*;
import org.example.petwif.domain.enums.AlbumSortType;
import org.example.petwif.domain.enums.FriendStatus;
import org.example.petwif.domain.enums.Scope;
import org.example.petwif.repository.*;
import org.example.petwif.repository.albumRepository.AlbumBookmarkRepository;
import org.example.petwif.repository.albumRepository.AlbumLikeRepository;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                       .map(c -> new CommentResponseDto(c, false))
                       .collect(Collectors.toList()))

               .build();
       }

    //=============================== 2. 앨범 조회 -> 메인 페이지 (스토리형식 + 게시글형식)================================//
    // 2-1. 앨범 조회 -> 스토리형식 조회
/*    @Override
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
    }*/

    //Slice 포함해서
    @Override
    public Slice<Album> getStoryAlbum(Long memberId, Integer page) {
       // 친구 목록 조회
        List<Friend> friendList = friendRepository.findByMember_IdAndStatus(memberId, FriendStatus.ACCEPTED);
        if (friendList.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }
        List<Long> friendIds = friendList.stream()
                .map(friend -> friend.getFriend().getId()).collect(Collectors.toList());

        Slice<Album> friendsAlbums = albumRepository.findByMemberIdInOrderByUpdatedAtDesc(friendIds, PageRequest.of(page, 10));
        List<Album> accessibleAlbums = friendsAlbums.getContent().stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId) && album.getScope() != Scope.MY)
                .collect(Collectors.toList());

        return new SliceImpl<Album>(accessibleAlbums, PageRequest.of(page, 10),friendsAlbums.hasNext());

    }


    // 2-2. 앨범 조회 -> 게시글 형식 조회
    @Override
    public Slice<AlbumResponseDto.MainPageAlbumResultDto> getMainpageAlbum(Long memberId, Integer page){

        //친구 인 사람 목록 조회, 아이디까지 찾았지 !! 여기서 빼주는거임!
        List<Friend> friendList = friendRepository.findByMember_IdAndStatus(memberId, FriendStatus.ACCEPTED);
        List<Long> friendIds = friendList.stream()
                .map(friend -> friend.getFriend().getId())
                .collect(Collectors.toList());

        friendIds.add(memberId); //나도 뻬줘야지!! 나는 추천 앨범에 있으면 안됨!

        Slice<Album> notFriendAlbums = albumRepository.findPublicAlbumsByNonFriends(friendIds, Scope.ALL, PageRequest.of(page, 10));
        List<AlbumResponseDto.MainPageAlbumResultDto> albumResultDtos = notFriendAlbums.getContent().stream()
                .filter(album -> albumCheckAccessService.checkAccessInBoolForSearch(album, memberId))
                .map(album ->convertToMainPageAlbumResultDto(album, memberId))
                .collect(Collectors.toList());

        if(notFriendAlbums.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }
        // 그사람들중에서 앨범 공개범위가 all인 앨범들을 slice에 저장
        //return notFriendAlbums.map(album -> convertToMainPageAlbumResultDto(album, memberId));
        return new SliceImpl<>(albumResultDtos, PageRequest.of(page,10), notFriendAlbums.hasNext());

    }

    private AlbumResponseDto.MainPageAlbumResultDto convertToMainPageAlbumResultDto(Album album, Long memberId) {
        return AlbumResponseDto.MainPageAlbumResultDto.builder()
                .albumId(album.getId())
                .content(album.getContent())
                .updatedAT(album.getUpdatedAt())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .nickName(Optional.ofNullable(album.getMember()).map(Member::getNickname).orElse("Unknown"))
                .profileImageUrl(Optional.ofNullable(album.getMember()).map(Member::getProfile_url).orElse(null))
                .memberId(album.getMember().getId())
                .isLiked(albumLikeRepository.existsByAlbumAndMemberId(album, memberId)) // 로그인한 사용자가 좋아요 했는지
                .isBookmarked(albumBookmarkRepository.existsByAlbumAndMemberId(album, memberId)) // 북마크 했는지
                .comments(commentRepository.findByAlbum(album).stream()
                        .map(c -> new CommentResponseDto(c, false))
                        .collect(Collectors.toList()))
                .build();
    }

    //=============================== 3. 탐색 페이지에서 앨범 조회 서비스================================//
    //08.22 [fix] 사실상 자신의 앨범을 제외하고 조회 가능한 앨범 전체 조회였습니다.
    // 근데 차단한 사용자의 앨범은 나오지 않게 하도록 추가 요구사항이 있어서 수정 하였습니다.
    @Override
    public Slice<Album> getSearchableAlbums(Long memberId, Integer page) {
        List<Member> allMemberList = memberRepository.findAll(); //일단 모든 사용자 다 가져와
        if (allMemberList.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList());
        }
        List<Long> allMemberIds = allMemberList.stream()
                .map(member -> member.getId()).collect(Collectors.toList()); // 그사용자의 memberId 다 가져와
        allMemberIds.remove(memberId); //자신의 앨범 제외! => 자신의 memberId 빼주고


        Slice<Album> allMemberAlbums = albumRepository.findAllByMemberIds(allMemberIds, PageRequest.of(page, 10)); //memberId로 앨범 다가져와
        List<Album> accessibleAlbums = allMemberAlbums.getContent().stream()
                .filter(album -> albumCheckAccessService.checkAccessInBoolForSearch(album, memberId) && album.getScope() != Scope.MY)
                .collect(Collectors.toList()); //그중 조회 가능한거 필터해줘


        return new SliceImpl<Album>(accessibleAlbums, PageRequest.of(page,10), allMemberAlbums.hasNext());

    }



    //=========================== 4. 특정 멤버의 앨범 페이지에서 앨범 조회 => 나, 다른사람 포함============================//
    @Override
    public Slice<AlbumResponseDto.UserAlbumViewDto> getMemberPageAlbums(Long pageOwnerId, Long currentUserId, Integer page, AlbumSortType sortType) {
        Slice<Album> allPageownerAlbums = albumRepository.findAlbumByMemberId(pageOwnerId, PageRequest.of(page, 10));


        List<AlbumResponseDto.UserAlbumViewDto> albumDtos = allPageownerAlbums.stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, currentUserId))
                .map(this::convertToUserAlbumDto)
                .sorted(getComparator(sortType))
                .collect(Collectors.toList());
        if(albumDtos.isEmpty()){
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }

        return new SliceImpl<>(albumDtos, PageRequest.of(page, 10), allPageownerAlbums.hasNext());
    }

    private AlbumResponseDto.UserAlbumViewDto convertToUserAlbumDto(Album album) {
        return AlbumResponseDto.UserAlbumViewDto.builder()
                .albumId(album.getId())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .bookmarkCount(album.getAlbumBookmarks().size())
                .commentCount(album.getCommentList().size())
                .updatedAT(album.getUpdatedAt())
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
                return Comparator.comparing(AlbumResponseDto.UserAlbumViewDto::getUpdatedAT).reversed(); // 최신순은 앨범 ID를 기준으로 역순 정렬
        }
    }

    public Slice<AlbumResponseDto.UserAlbumViewDto> getSearchedMemberPageAlbums(Long pageOwnerId, Long currentUserId, Integer page, AlbumSortType sortType, String albumTitle){
        if(albumTitle == null ||albumTitle.trim().isEmpty()) {
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }
        Slice<Album> allPageownerAlbums = albumRepository.findAlbumByMemberIdAndTitleContaining(pageOwnerId, albumTitle, PageRequest.of(page, 10));

        List<AlbumResponseDto.UserAlbumViewDto> albumDtos = allPageownerAlbums.stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, currentUserId))
                .map(this::convertToUserAlbumDto)
                .sorted(getComparator(sortType))
                .collect(Collectors.toList());
        if(albumDtos.isEmpty()){
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }

        return new SliceImpl<>(albumDtos, PageRequest.of(page, 10), allPageownerAlbums.hasNext());
    }



    //=================================== 5. 북마크한 앨범 에서 앨범 조회 ====================================//
    @Override
    public Slice<AlbumResponseDto.MemberBookmarkAlbumDto> getMemberBookmarkAlbums(Long memberId, Integer page, AlbumSortType sortType){
        // 접근 가능한 앨범 중에서 사용자가 북마크한 앨범만 필터링
        List<Long> bookmarkedAlbumIds = albumBookmarkRepository.findBookmarkedAlbumIdsByMemberId(memberId);

        Slice<Album> thisMemberBookmarkedAlbums = albumRepository.findAllByIds(bookmarkedAlbumIds, PageRequest.of(page,10));

        List<AlbumResponseDto.MemberBookmarkAlbumDto> accessibleAlbums = thisMemberBookmarkedAlbums.getContent().stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId))
                .map(this::convertToMemberBookmarkAlbumDto)
                .sorted(getBookmarkComparator(sortType))
                .collect(Collectors.toList());

        if (accessibleAlbums.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }
        return new SliceImpl<>(accessibleAlbums, PageRequest.of(page, 10), thisMemberBookmarkedAlbums.hasNext());
    }

    private AlbumResponseDto.MemberBookmarkAlbumDto convertToMemberBookmarkAlbumDto(Album album) {
        return AlbumResponseDto.MemberBookmarkAlbumDto.builder()
                .albumId(album.getId())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .bookmarkCount(album.getAlbumBookmarks().size())
                .commentCount(album.getCommentList().size())
                .updatedAT(album.getUpdatedAt())
                .build();
    }


    private Comparator<AlbumResponseDto.MemberBookmarkAlbumDto> getBookmarkComparator(AlbumSortType sortType) {
        switch (sortType) {
            case LIKES:
                return Comparator.comparingInt(AlbumResponseDto.MemberBookmarkAlbumDto::getLikeCount).reversed();
            case COMMENTS:
                return Comparator.comparingInt(AlbumResponseDto.MemberBookmarkAlbumDto::getCommentCount).reversed();
            case BOOKMARKS:
                return Comparator.comparingInt(AlbumResponseDto.MemberBookmarkAlbumDto::getBookmarkCount).reversed();
            case LATEST:
            default:
                return Comparator.comparing(AlbumResponseDto.MemberBookmarkAlbumDto::getUpdatedAT).reversed();
        }
    }

    //=================================== 5. 북마크한 앨범 에서 앨범 제목을 검색한 앨범 조회 ====================================//

    public Slice<AlbumResponseDto.MemberBookmarkAlbumDto> getSearchedMemberBookmarkAlbums(Long memberId, Integer page, String albumTitle, AlbumSortType sortType){
        // 접근 가능한 앨범 중에서 사용자가 북마크한 앨범만 필터링
        if(albumTitle == null || albumTitle.trim().isEmpty()) {
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }

        List<Long> bookmarkedAlbumIds = albumBookmarkRepository.findBookmarkedAlbumIdsByMemberId(memberId);

        Slice<Album> thisMemberBookmarkedAlbums = albumRepository.findAllByIdsAndTitleContaining(bookmarkedAlbumIds, albumTitle, PageRequest.of(page,10));

        List<AlbumResponseDto.MemberBookmarkAlbumDto> accessibleAlbums = thisMemberBookmarkedAlbums.getContent().stream()
                .filter(album -> albumCheckAccessService.checkAccessInBool(album, memberId))
                .map(this::convertToMemberBookmarkAlbumDto)
                .sorted(getBookmarkComparator(sortType))
                .collect(Collectors.toList());

        if (accessibleAlbums.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList()); // 비어있는 슬라이스 반환
        }
        return new SliceImpl<>(accessibleAlbums, PageRequest.of(page, 10), thisMemberBookmarkedAlbums.hasNext());
    }


}
