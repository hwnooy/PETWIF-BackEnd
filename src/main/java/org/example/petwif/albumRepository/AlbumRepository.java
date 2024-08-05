package org.example.petwif.albumRepository;

import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    public List<Album> findAlbumsByTitle(String title);
    public List<Album> findAlbumsByMemberId(Long memberId);
    public List<Album> findAlbumsByMemberName(String name);



}
