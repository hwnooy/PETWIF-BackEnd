package org.example.petwif.web.dto.MemberDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailLoginAccessTokenResponse {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String nickname;
}
