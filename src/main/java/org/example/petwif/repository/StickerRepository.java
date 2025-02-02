package org.example.petwif.repository;

import org.example.petwif.domain.entity.Sticker;
import org.example.petwif.domain.enums.StickerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface StickerRepository extends JpaRepository<Sticker, Long> {
    List<Sticker> findStickersByStickerType(StickerType type);

    @Query("SELECT s FROM Sticker s WHERE s.id = :id")
    Sticker findById(@Param("id") long id);

}
