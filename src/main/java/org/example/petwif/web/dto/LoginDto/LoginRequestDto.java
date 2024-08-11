package org.example.petwif.web.dto.LoginDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequestDto {
    private String name;
    private String email;
}


