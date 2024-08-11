package org.example.petwif.web.dto.LoginDto;

import lombok.Builder;
import lombok.Getter;
import org.example.petwif.domain.enums.Gender;
import org.example.petwif.domain.enums.Telecom;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberEtcInfoRequestDto {
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime birthDate;

    private Telecom telecom;
    private String phoneNum;
    private String address;
}
