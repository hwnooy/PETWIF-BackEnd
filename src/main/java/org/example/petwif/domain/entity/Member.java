package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.enums.Gender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profile_url;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime birthDate;

    private String phoneNumber;

    private String address;

    private String status;

    //LocalDateTime 사용하면 될까요?
    //private LocalDateTime inactiveDate;

    private boolean autoLogin;


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Block> blockList = new ArrayList<>();
}
