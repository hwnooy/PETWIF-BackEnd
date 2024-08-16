package org.example.petwif.repository;

import org.example.petwif.domain.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface StickerRepository extends JpaRepository<Sticker, Long> {
}