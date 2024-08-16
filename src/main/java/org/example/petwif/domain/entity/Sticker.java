package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.enums.StickerType;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sticker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stickerName;

    private String stickerUrl;

    @Enumerated(EnumType.STRING)
    private StickerType stickerType;



}
