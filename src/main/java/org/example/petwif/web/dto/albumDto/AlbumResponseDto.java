package org.example.petwif.web.dto.albumDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.entity.AlbumImage;
import org.example.petwif.domain.enums.Scope;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


public class AlbumResponseDto {


    //==앨범 CRUD==//
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveResultDto{
        Long albumId;
        Long memberId;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResultDto{
        Long albumId;
        LocalDateTime updatedAt;
    }

    //== 앨범 조회!!!!!==//

    // 1. 앨범 세부 페이지 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResultDto {
        private Long albumId;
        private String title;
        private String content;
        private Scope scope;
        private LocalDateTime updatedAt;
        private String coverImageUrl;
        private String albumImages;
        private int viewCount;
        private int likeCount;
        private int everyCommentCount;
        private int bookmarkCount;
        private boolean isLiked;
        private boolean isBookmarked;
        private List<CommentResponseDto> comments;

    }

    //====================================================================//
    // 2-1. 메인 페이지 내에서 앨범 스토리 형식 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryAlbumResultDto{
         Long albumId;
         String nickName;
         String profileImageUrl;
         LocalDateTime updatedAT;
    }

    //2-2 메인 페이지 내의 앨범 스토리들을 리스트로 합치는 dto

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryAlbumListDto{
        List<StoryAlbumResultDto> stories;
         Integer listSize;
         boolean isFirst;
         boolean isLast;
         boolean hasNext;
    }

    // 2-3. 메인 페이지 내에서 앨범 게시글 형식 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageAlbumResultDto {
         Long albumId;
         String content;
         LocalDateTime updatedAT;
         String coverImageUrl;
         int likeCount;
         String nickName;
         String profileImageUrl;
         Long memberId;
         boolean isLiked;
         boolean isBookmarked;
         List<CommentResponseDto> comments;
    }

    // 2-4. 메인 페이지의 앨범 들을 리스트로 변환 해주는 dto
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageAlbumListDto{
        private List<MainPageAlbumResultDto> mainpageAlbums;
        Integer listSize;
        boolean isFirst;
        boolean isLast;
        boolean hasNext;
    }

    //====================================================================//
    // 3. 탐색 페이지에서 앨범 조회 dto
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchAlbumDto {
        private Long albumId;
        private String coverImageUrl;
        private int likeCount;
        private int bookmarkCount;
        private int commentCount;
        private LocalDateTime updatedAT;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchAlbumListDto{
        private List<SearchAlbumDto> searchAlbums;
        Integer listSize;
        boolean isFirst;
        boolean isLast;
        boolean hasNext;
    }

    //====================================================================//
    // 4. 특정 멤버의 앨범 페이지에서 앨범 조회 => 나, 다른사람 포함

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAlbumViewDto {
        private Long albumId;
        private String coverImageUrl;
        private int likeCount;
        private int bookmarkCount;
        private int commentCount;
        private LocalDateTime updatedAT;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAlbumViewListDto {
        private List<UserAlbumViewDto> albums;
        Integer listSize;
        boolean isFirst;
        boolean isLast;
        boolean hasNext;
        private int totalAlbumCount;
    }
    //====================================================================//
    // 5. 북마크한 앨범에서 앨범 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberBookmarkAlbumDto {
        private Long albumId;
        private String coverImageUrl;
        private int likeCount;
        private int bookmarkCount;
        private int commentCount;
        private LocalDateTime updatedAT;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberBookmarkAlbumListDto{
        private List<MemberBookmarkAlbumDto> bookmarkedAlbums;
        Integer listSize;
        boolean isFirst;
        boolean isLast;
        boolean hasNext;
    }


    //==앨범 좋아요==//
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeResultDto{
        private Long memberId;
        private String memberNickname;
        private String profileUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeListDto{
        private List<LikeResultDto> likes;
        Integer listSize;
        boolean isFirst;
        boolean isLast;
        boolean hasNext;
    }

    //==앨범 북마크, 특정 앨범에 들어가서 북마크 확인할 때==//
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkResultDto{
        private Long memberId;
        private String memberNickName;
        private String profileUrl;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkListDto{
        private List<BookmarkResultDto> bookmarks;
        Integer listSize;
        boolean isFirst;
        boolean isLast;
        boolean hasNext;
    }
}