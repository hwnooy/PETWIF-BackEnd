package org.example.petwif.albumRepository;

import org.example.petwif.domain.entity.AlbumLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumLikeRepository extends JpaRepository<AlbumLike, Long> {
}
