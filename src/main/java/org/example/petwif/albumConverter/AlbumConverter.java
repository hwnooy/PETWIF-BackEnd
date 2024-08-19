package org.example.petwif.albumConverter;

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

   /* public static AlbumResponseDto.StoryAlbumResultDto convertToStoryAlbumResultDto(Album album){
        return AlbumResponseDto.StoryAlbumResultDto.builder()
                .albumId(album.getId())
                .nickName(album.getMember().getNickname())
                .profileImageUrl(Optional.of(album.getMember().getProfile_url()).orElse(null))
                .build();
    }
*/
/*    public static AlbumResponseDto.StoryAlbumListDto convertToStoryAlbumResultListDto(Slice<Album> stories){
        List<AlbumResponseDto.StoryAlbumResultDto> storyAlbumResultDtoList = stories.stream()
                .map(AlbumConverter::convertToStoryAlbumResultDto).collect(Collectors.toList());

        return AlbumResponseDto.StoryAlbumListDto.builder()
                .stories(storyAlbumResultDtoList)
                .listSize(stories.getSize())
                .isFirst(stories.isFirst())
                .isLast(stories.isLast())
                .hasNext(stories.hasNext())
                .build();
    }*/



}
