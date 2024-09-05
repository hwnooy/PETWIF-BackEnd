package org.example.petwif.web.dto.MemberDto;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponseDto {
    private Long id;
    private String profile_url;
    private String name;
    private String nickname;
}
