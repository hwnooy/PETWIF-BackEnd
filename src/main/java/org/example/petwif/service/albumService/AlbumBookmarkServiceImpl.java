package org.example.petwif.service.albumService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.AlbumBookmark;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.AlbumRepository;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.albumRepository.AlbumBookmarkRepository;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumBookmarkServiceImpl implements AlbumBookmarkService{
    private final AlbumRepository albumRepository;
    private final AlbumBookmarkRepository albumBookmarkRepository;
    private final MemberRepository memberRepository;


    private final AlbumCheckAccessService albumCheckAccessService;

    //북마크 생성
    @Transactional
    @Override
    public void addBookmark(Long albumId, Long memberId){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        albumCheckAccessService.checkAccess(album, memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        albumBookmarkRepository.findByAlbumAndMember(album, member).ifPresent(report -> {
            throw new GeneralException(ErrorStatus.ALBUM_BOOKMARK_EXIST);
        });
        AlbumBookmark albumBookmark = AlbumBookmark.builder()
                                .album(album)
                                .member(member)
                                .build();
        albumBookmarkRepository.save(albumBookmark);
    }

    //북마크 삭제

    @Transactional
    @Override
    public void deleteBookmark(Long albumId, Long memberId){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        albumCheckAccessService.checkAccess(album, memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        AlbumBookmark albumBookmark = albumBookmarkRepository.findByAlbumAndMember(album, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_BOOKMARK_NOT_FOUND));
       albumBookmarkRepository.delete(albumBookmark);
    }

    // 북마크 리스트 조회
    @Override
    public List<AlbumResponseDto.BookmarkResultDto> getAlbumBookmarks(Long albumId, Long memberId){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        albumCheckAccessService.checkAccess(album, memberId);
        List<AlbumBookmark> bookmarks = albumBookmarkRepository.findByAlbum(album);
        if(bookmarks.isEmpty())throw new GeneralException(ErrorStatus.ALBUM_BOOKMARK_PAGE_NOT_FOUND);
        return bookmarks.stream()
                .map(bookmark ->new AlbumResponseDto.BookmarkResultDto(bookmark.getMember().getId(), bookmark.getMember().getNickname(), bookmark.getMember().getProfile_url()))
                .collect(Collectors.toList());
    }
}
