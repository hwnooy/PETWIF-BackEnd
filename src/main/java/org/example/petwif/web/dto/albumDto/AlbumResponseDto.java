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
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResultDto{
        LocalDateTime createdAt;
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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private AlbumImage coverImage;
        private List<AlbumImage> albumImages;
        private int viewCount;
        private int likeCount;
        private int everyCommentCount;
        private int bookmarkCount;
        private List<CommentResponseDto> comments;
    }

    //====================================================================//
    // 2-1. 메인 페이지 내에서 앨범 스토리 형식 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryAlbumResultDto{
        private Long albumId;
        private String Nickname;
        private String profileImageUrl;
    }

    //2-2 메인 페이지 내의 앨범 스토리들을 리스트로 합치는 dto

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryAlbumListDto{
        private List<StoryAlbumResultDto> stories;

    }

    // 2-2. 메인 페이지 내에서 앨범 게시글 형식 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageAlbumResultDto {
        private Long albumId;
        private String content;
        private Scope scope;
        private LocalDateTime createdAt;// 언제 업데이트 했는지만 보이게 할거니까, 나중에 지워야함
        private LocalDateTime updatedAt;
        private AlbumImage coverImage;
        private int likeCount;
        // private List<AlbumImage> albumImages; 메인페이지는 표지만 보인다. 이건 안보인다.

        // private String title; 메인페이지는 제목 안보인다
        // private int viewCount; 메인페이지는 조회수 안보인다

        //private int everyCommentCount; 메인페이지는 댓글수 안보인다
      //  private int bookmarkCount; 메인페이지는 북마크수 안보인다
       // private List<CommentResponseDto> comments;

    }

    // 메인 페이지의 앨범 들을 리스트로 변환 해주는 dto

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageAlbumListDto{
        private List<MainPageAlbumResultDto> mainpageAlbums;

    }

    // 2-3. 위에 두개 합친거!
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageContentDto{
        private StoryAlbumListDto storyAlbumListDto;
        private MainPageAlbumListDto mainpageAlbumListDto;
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
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchAlbumListDto{
        private List<SearchAlbumDto> albums;
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
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberBookmarkAlbumListDto{
        private List<MemberBookmarkAlbumDto> albums;
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
    }
}