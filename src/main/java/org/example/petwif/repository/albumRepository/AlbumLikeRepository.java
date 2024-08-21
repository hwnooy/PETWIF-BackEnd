package org.example.petwif.repository.albumRepository;

import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.AlbumLike;
import org.example.petwif.domain.entity.Member;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumLikeRepository extends JpaRepository<AlbumLike, Long> {
    Optional<AlbumLike> findByAlbumIdAndMemberId(Long albumId, Long memberId); //예외 처리를 위해 추가
    int countByAlbum(Album album);

    Slice<AlbumLike> findByAlbum(Album album, PageRequest pageRequest);

    boolean existsByAlbumAndMemberId(Album album, Long memberId);
}
