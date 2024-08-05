package org.example.petwif.albumRepository;

import org.example.petwif.domain.entity.AlbumImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumImageRepository extends JpaRepository<AlbumImage, Long> {
    List<AlbumImage> findAlbumImageByAlbumId(Long albumId);
}
