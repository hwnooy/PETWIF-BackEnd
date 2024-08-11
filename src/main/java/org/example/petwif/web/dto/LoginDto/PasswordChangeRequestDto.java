package org.example.petwif.web.dto.LoginDto;

import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {
    private String changePW;
    private String checkChangePw;
}
