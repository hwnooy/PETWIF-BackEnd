package org.example.petwif.web.dto.MemberDto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordChangeRequestDto {
    @Size(min=12,  message = "비밀번호는 최소 12자 이상이어야 합니다.")
    private String changePW;
    @Size(min=12,  message = "비밀번호는 최소 12자 이상이어야 합니다.")
    private String checkChangePw;
}
