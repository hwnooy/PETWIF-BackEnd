package org.example.petwif.repository.albumRepository;

import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.AlbumLike;
import org.example.petwif.domain.entity.AlbumReport;
import org.example.petwif.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumReportRepository extends JpaRepository<AlbumReport, Long> {
    Optional<AlbumReport> findByAlbumAndMember(Album album, Member member); //예외 처리를 위해 추가
}
