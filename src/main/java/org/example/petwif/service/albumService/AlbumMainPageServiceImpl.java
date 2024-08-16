/*
package org.example.petwif.service.albumService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Comment;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.enums.Scope;
import org.example.petwif.repository.AlbumRepository;
import org.example.petwif.repository.FriendRepository;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumMainPageServiceImpl implements AlbumMainPageService{
    private final FriendRepository friendRepository;
    private final AlbumRepository albumRepository;

  */
/*  @Override
    public List<Album> getFriendAlbums(Long memberId) {
        List<Member> friends = friendRepository.findAllFriendsByMemberId(memberId);
        return albumRepository.findAlbumsByMember(friends, Scope.FRIEND);
    }*//*


    public List<Album> getPublicAlbums() {
        return albumRepository.findAlbumsByScope(Scope.ALL);
    }

    public AlbumResponseDto.StoryAlbumListDto convertToStoryAlbumListDto(List<Album> albums) {
        List<AlbumResponseDto.StoryAlbumResultDto> results = albums.stream()
                .map(album -> new AlbumResponseDto.StoryAlbumResultDto(
                        album.getId(),
                        album.getMember().getNickname(),
                        album.getMember().getProfile_url()))
                .collect(Collectors.toList());
        return new AlbumResponseDto.StoryAlbumListDto(results);
    }

    public AlbumResponseDto.MainPageAlbumListDto convertToMainPageAlbumListDto(List<Album> albums) {
        List<AlbumResponseDto.MainPageAlbumResultDto> results = albums.stream()
                .map(album -> AlbumResponseDto.MainPageAlbumResultDto.builder()
                        .albumId(album.getId())
                        .content(album.getContent())
                        .scope(album.getScope())
                        .createdAt(album.getCreatedAt())
                        .updatedAt(album.getUpdatedAt())
                        .coverImage(album.getCoverImage())
                        .likeCount(album.getAlbumLikes().size())
                       // .comments((album.getCommentList())) // 댓글 DTO 변환 호출
                        .build())
                .collect(Collectors.toList());
        return new AlbumResponseDto.MainPageAlbumListDto(results);
    }

}
*/
