package org.example.petwif.web.dto.ChatDTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    private Long chatRoomId; // 방 번호
    private Long memberId; // 채팅을 보낸 사람
}
