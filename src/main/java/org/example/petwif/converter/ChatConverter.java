package org.example.petwif.converter;

import org.example.petwif.domain.entity.Chat;
import org.example.petwif.domain.entity.ChatImage;
import org.example.petwif.domain.entity.ChatRoom;
import org.example.petwif.web.dto.ChatDTO.ChatRequestDTO;
import org.example.petwif.web.dto.ChatDTO.ChatResponseDTO;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ChatConverter {
    public static ChatRoom toChatRoom(ChatRequestDTO.CreateChatRoomDTO request) {
        return ChatRoom.builder()
                .build();
    }

    public static ChatResponseDTO.CreateChatRoomResultDTO createChatRoomResultDTO(ChatRoom chatRoom) { //채팅 생성
        return ChatResponseDTO.CreateChatRoomResultDTO.builder()
                .chatRoomId(chatRoom.getId())
                .createdAt(LocalDateTime.now())
                .build();

    }

    public static Chat toChat(ChatRequestDTO.SendChatDTO request) {
        return Chat.builder()
                .content(request.getContent())
                .build();
    }

    public static ChatResponseDTO.SendChatResultDTO sendChatResultDTO(Chat chat) { //채팅 보내기
        return ChatResponseDTO.SendChatResultDTO.builder()
                .chatId(chat.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ChatImage toChatImage(String imageUrl, Chat chat){ //채팅 사진
        return ChatImage.builder()
                .imageUrl(imageUrl)
                .chat(chat)
                .build();
    }

    public static ChatResponseDTO.ChatPreviewDTO toChatPreviewDTO(Chat chat) { //채팅창 화면 조회
        return ChatResponseDTO.ChatPreviewDTO.builder()
                .chatId(chat.getId())
                .memberId(chat.getMember().getId())
                .content(chat.getContent())
                .imageUrl(chat.getMember().getProfile_url())
                .nickName(chat.getMember().getNickname())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ChatResponseDTO.ChatPreviewListDTO toChatPreviewListDTO(Slice<Chat> chatList) {
        List<ChatResponseDTO.ChatPreviewDTO> chatPreviewDTOList = chatList.stream()
                .map(ChatConverter::toChatPreviewDTO).collect(Collectors.toList());

        return ChatResponseDTO.ChatPreviewListDTO.builder()
                .chatList(chatPreviewDTOList)
                .listSize(chatList.getSize())
                .isFirst(chatList.isFirst())
                .isLast(chatList.isLast())
                .hasNext(chatList.hasNext())
                .build();
    }

    public static ChatResponseDTO.ChatRoomPreviewDTO toChatRoomPreviewDTO(ChatRoom chatRoom) { //채팅방 목록 조회
        return ChatResponseDTO.ChatRoomPreviewDTO.builder()
                .memberId(chatRoom.getMember().getId())
                .otherId(chatRoom.getOther().getId())
                .chatRoomId(chatRoom.getId())
                .roomName(chatRoom.getMember().getNickname())
                .imageUrl(chatRoom.getMember().getProfile_url())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ChatResponseDTO.ChatRoomPreviewListDTO toChatRoomPreviewListDTO(Slice<ChatRoom> chatRoomList) {
        List<ChatResponseDTO.ChatRoomPreviewDTO> chatRoomPreviewDTOList = chatRoomList.stream()
                .map(ChatConverter::toChatRoomPreviewDTO).collect(Collectors.toList());

       return ChatResponseDTO.ChatRoomPreviewListDTO.builder()
               .chatRoomList(chatRoomPreviewDTOList)
               .listSize(chatRoomList.getSize())
               .isFirst(chatRoomList.isFirst())
               .isLast(chatRoomList.isLast())
               .hasNext(chatRoomList.hasNext())
               .build();
    }

    public static ChatResponseDTO.LastChatPreviewDTO toLastChatPreviewDTO(Chat chat) { //가장 최근 채팅 조회
        return ChatResponseDTO.LastChatPreviewDTO.builder()
                .chatRoomId(chat.getChatRoom().getId())
                .memberId(chat.getMember().getId())
                .content(chat.getContent())
                .time(LocalDateTime.now())
                .build();
    }
}
