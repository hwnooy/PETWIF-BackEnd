package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.enums.Gender;

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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    private String address;

    private String status;

    //LocalDateTime 사용하면 될까요?
    //private LocalDateTime inactiveDate;

    private boolean autoLogin;

}
