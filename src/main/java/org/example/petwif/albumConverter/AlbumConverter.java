package org.example.petwif.albumConverter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.AlbumImage;
import org.example.petwif.domain.entity.Comment;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.CommentRepository;
import org.example.petwif.repository.albumRepository.AlbumBookmarkRepository;
import org.example.petwif.repository.albumRepository.AlbumLikeRepository;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumRequestDto;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlbumConverter {


    public static AlbumResponseDto.SaveResultDto toAlbumResultDto(Album album, Member member){
        return AlbumResponseDto.SaveResultDto.builder()
                .albumId(album.getId())
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static AlbumResponseDto.UpdateResultDto UpdatedAlbumResultDto(Album album){
        return AlbumResponseDto.UpdateResultDto.builder()
                .albumId(album.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public static Album toAlbumEntity(AlbumRequestDto.SaveRequestDto requestDto, Member member) {
        return Album.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .scope(requestDto.getScope())
                .view(0)
                .build();
    }

    public static AlbumResponseDto.StoryAlbumResultDto convertToStoryAlbumResultDto(Album album) {
        return AlbumResponseDto.StoryAlbumResultDto.builder()
                .albumId(album.getId())
                .nickName(album.getMember() != null ? album.getMember().getNickname() : null) // 멤버 정보가 null이면 null 반환
                .profileImageUrl(Optional.ofNullable(album.getMember()).map(Member::getProfile_url).orElse(null))
                .updatedAT(album.getUpdatedAt())
                .build();
    }


    public static AlbumResponseDto.StoryAlbumListDto convertToStoryAlbumResultListDto(Slice<Album> stories){
        List<AlbumResponseDto.StoryAlbumResultDto> storyAlbumResultDtoList = stories.stream()
                .map(AlbumConverter::convertToStoryAlbumResultDto).collect(Collectors.toList());

        return AlbumResponseDto.StoryAlbumListDto.builder()
                .stories(storyAlbumResultDtoList)
                .listSize(stories.getSize())
                .isFirst(stories.isFirst())
                .isLast(stories.isLast())
                .hasNext(stories.hasNext())
                .build();
    }

    //service에서 convert함
    /*public static AlbumResponseDto.MainPageAlbumResultDto convertToMainpageAlbumResultDto(Album album,
                                                                                      List<Comment> comments,
                                                                                          boolean isLiked,
                                                                                          boolean isBookmarked){
        return AlbumResponseDto.MainPageAlbumResultDto.builder()
                .albumId(album.getId())
                .content(album.getContent())
                .updatedAT(album.getUpdatedAt())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .nickName(Optional.ofNullable(album.getMember()).map(Member::getNickname).orElse("Unknown"))
                .profileImageUrl(Optional.ofNullable(album.getMember()).map(Member::getProfile_url).orElse(null))
                .memberId(album.getMember().getId())
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .comments(comments.stream()
                        .map(c -> new CommentResponseDto(c))
                        .collect(Collectors.toList()))
                .build();
    }*/

    public static AlbumResponseDto.MainPageAlbumListDto convertToMainpageAlbumResultListDto(Slice<AlbumResponseDto.MainPageAlbumResultDto> albumSlice){
        List<AlbumResponseDto.MainPageAlbumResultDto> albums = Optional.ofNullable(albumSlice.getContent())
                .orElse(Collections.emptyList());  // getContent가 null이면 빈 리스트 반환

        return AlbumResponseDto.MainPageAlbumListDto.builder()
                .mainpageAlbums(albums)
                .listSize(albums.size())
                .isFirst(albumSlice.isFirst())
                .isLast(albumSlice.isLast())
                .hasNext(albumSlice.hasNext())
                .build();
    }

    public static AlbumResponseDto.SearchAlbumDto convertToSearchAlbumDto(Album album){
        return AlbumResponseDto.SearchAlbumDto.builder()
                .albumId(album.getId())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .bookmarkCount(album.getAlbumBookmarks().size())
                .commentCount(album.getCommentList().size())
                .updatedAT(album.getUpdatedAt())
                .build();
    }

    public static AlbumResponseDto.SearchAlbumListDto convertToSearchAlbumListDto(Slice<Album> searches){
        List<AlbumResponseDto.SearchAlbumDto> searchAlbumDtoList = searches.stream()
                .map(AlbumConverter::convertToSearchAlbumDto).collect(Collectors.toList());

        return AlbumResponseDto.SearchAlbumListDto.builder()
                .searchAlbums(searchAlbumDtoList)
                .listSize(searches.getSize())
                .isFirst(searches.isFirst())
                .isLast(searches.isLast())
                .hasNext(searches.hasNext())
                .build();
    }

    public static AlbumResponseDto.MemberBookmarkAlbumDto convertToMemberBookmarkAlbumDto(Album album){
        return AlbumResponseDto.MemberBookmarkAlbumDto.builder()
                .albumId(album.getId())
                .coverImageUrl(Optional.ofNullable(album.getCoverImage()).map(AlbumImage::getImageURL).orElse(null))
                .likeCount(album.getAlbumLikes().size())
                .bookmarkCount(album.getAlbumBookmarks().size())
                .commentCount(album.getCommentList().size())
                .updatedAT(album.getUpdatedAt())
                .build();
    }

    public static AlbumResponseDto.MemberBookmarkAlbumListDto convertToMemberBookmarkAlbumListDto(Slice<Album> bookmarkedAlbums){
            List<AlbumResponseDto.MemberBookmarkAlbumDto> memberBookmarkAlbumDtoList = bookmarkedAlbums.stream()
                .map(AlbumConverter::convertToMemberBookmarkAlbumDto).collect(Collectors.toList());
            return AlbumResponseDto.MemberBookmarkAlbumListDto.builder()
                    .bookmarkedAlbums(memberBookmarkAlbumDtoList)
                    .listSize(bookmarkedAlbums.getSize())
                    .isFirst(bookmarkedAlbums.isFirst())
                    .isLast(bookmarkedAlbums.isLast())
                    .hasNext(bookmarkedAlbums.hasNext())
                    .build();
    }



}
