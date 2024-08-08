package org.example.petwif.web.dto.LoginDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRegistrationDto {
    private String name;
    private String email;
    private String pw;
    private String pwCheck;
}
