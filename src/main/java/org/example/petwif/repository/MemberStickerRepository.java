package org.example.petwif.repository;

import org.example.petwif.domain.entity.MemberSticker;
import org.example.petwif.domain.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberStickerRepository extends JpaRepository<MemberSticker, Long> {
    List<MemberSticker> findByMemberId(Long memberId);
}
