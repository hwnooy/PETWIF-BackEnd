package org.example.petwif.web.dto.MemberDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequestDto {
    private String email;
    private String pw;
}


