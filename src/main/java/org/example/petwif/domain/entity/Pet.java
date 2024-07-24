package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.enums.Gender;
import org.example.petwif.domain.enums.PetGender;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String petName;

    @Enumerated(EnumType.STRING)
    private PetGender petGender;

    private int petAge;

    private String petKind;

    private String etc;
}
