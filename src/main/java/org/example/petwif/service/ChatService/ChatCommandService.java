package org.example.petwif.service.ChatService;

import org.example.petwif.domain.entity.Chat;
import org.example.petwif.domain.entity.ChatReport;
import org.example.petwif.domain.entity.ChatRoom;
import org.example.petwif.web.dto.ChatDTO.ChatRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ChatCommandService {

    //채팅 생성
    ChatRoom createChatRoom(Long memberId, Long otherId, ChatRequestDTO.CreateChatRoomDTO request);

    //채팅 보내기
    Chat sendChat(Long memberId, Long chatRoomId, ChatRequestDTO.SendChatDTO request, MultipartFile chatImage);

    //채팅방 나가기 = memberChatRoom
    void deleteChatRoom(Long memberId, Long chatRoomId);

    //채팅 신고
    ChatReport reportChat(Long memberId, Long chatRoomId, Long chatId, ChatRequestDTO.ReportChatDTO request);

}
