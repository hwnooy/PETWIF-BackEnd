package org.example.petwif.web.dto.MemberDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder
public class MemberEtcInfoRequestDto {
    // Member entity 속성과 똑같이 설정해줘야하고 값 set할때 Enum으로 바꿔줘야함
    @Enumerated(EnumType.STRING)
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private String telecom;
    private String phoneNum;
    private String address;
}
