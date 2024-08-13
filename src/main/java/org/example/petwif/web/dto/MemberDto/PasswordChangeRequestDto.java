package org.example.petwif.web.dto.MemberDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordChangeRequestDto {
    private String changePW;
    private String checkChangePw;
}
