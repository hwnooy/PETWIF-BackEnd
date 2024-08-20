package org.example.petwif.service.ChatService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.S3.Uuid;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.converter.ChatConverter;
import org.example.petwif.domain.entity.*;
import org.example.petwif.domain.enums.ChatRoomStatus;
import org.example.petwif.repository.*;
import org.example.petwif.web.dto.ChatDTO.ChatRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatCommandServiceImpl implements ChatCommandService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final ChatImageRepository chatImageRepository;
    private final ChatReportRepository chatReportRepository;
    private final BlockRepository blockRepository;

    @Override
    @Transactional
    public ChatRoom createChatRoom(Long memberId, Long otherId, ChatRequestDTO.CreateChatRoomDTO request) { //채팅 생성
        ChatRoom chatRoom = new ChatRoom();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Member other = memberRepository.findById(otherId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        chatRoom.setMember(member);
        chatRoom.setOther(other);

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public Chat sendChat(Long memberId, Long chatRoomId, ChatRequestDTO.SendChatDTO request) { //채팅 보내기
        Chat chat = ChatConverter.toChat(request);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHATROOM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!chatRoom.getMember().equals(member) && !chatRoom.getOther().equals(member)) { //채팅방에 없는 사용자가 채팅 메시지를 보내려고 할 경우
            throw new GeneralException(ErrorStatus.CHAT_ACCESS_RESTRICTED);
        }

        if (blockRepository.existsByMember_IdAndTarget_Id(chatRoom.getMember().getId(), chatRoom.getOther().getId())) { //차단 여부 확인 -> 차단한 사용자가 채팅 메시지를 보내려고 할 경우
            throw new GeneralException(ErrorStatus.CHAT_ACCESS_RESTRICTED);
        }

        if (chatRoom.getChatRoomStatus() == ChatRoomStatus.INACTIVE) { //채팅방이 비활성화 상태일 경우, 다시 활성화 상태로
            chatRoom.setChatRoomStatus(ChatRoomStatus.ACTIVE);
            chatRoom.setMemberStatus(false);
            chatRoom.setOtherStatus(false);
            chatRoomRepository.save(chatRoom);
        }

        chat.setChatRoom(chatRoom);
        chat.setMember(member);

        //채팅 사진 전송
        if (request.getChatImages() != null && !request.getChatImages().isEmpty()) {
            String uuid = UUID.randomUUID().toString() + ".jpg";
            Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

            String pictureUrl = s3Manager.uploadFile(s3Manager.generateChatKeyName(savedUuid), request.getChatImages());
            ChatImage chatImage = ChatConverter.toChatImage(pictureUrl, chat);

            chatImage.setChat(chat);
            chat.setChatImage(chatImage);

            chatImage.setImageUrl(chatImage.getImageUrl());

            chatImageRepository.save(chatImage);
        }
        return chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void deleteChatRoom(Long memberId, Long chatRoomId) { //채팅방 나가기
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHATROOM_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //member와 other 중 한 명이 나가면 INACTIVE로 설정
        if (chatRoom.getMember().getId().equals(memberId)) {
            chatRoom.setMemberStatus(true);
        } else if (chatRoom.getOther().getId().equals(memberId)) {
            chatRoom.setOtherStatus(true);
        } else {
            throw new IllegalArgumentException("Member not found in this chat room");
        }

        //둘 다 나갈 시, 채팅방 삭제
        if (chatRoom.isMemberStatus() && chatRoom.isOtherStatus()){
            chatRoomRepository.delete(chatRoom);
        } else {
            chatRoom.setChatRoomStatus(ChatRoomStatus.INACTIVE);
            chatRoomRepository.save(chatRoom);
        }
    }

    @Override
    @Transactional
    public ChatReport reportChat(Long memberId, Long chatRoomId, Long chatId, ChatRequestDTO.ReportChatDTO request) { //채팅 신고
        ChatReport chatReport = ChatConverter.toChatReport(request);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHATROOM_NOT_FOUND));
        Chat chat = chatRepository.findChatById(chatId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_NOT_FOUND));


        if (!chatRoom.getMember().equals(member) && !chatRoom.getOther().equals(member)) { //채팅방에 없는 사용자가 채팅 메시지를 신고하려고 할 경우
            throw new GeneralException(ErrorStatus.CHAT_ACCESS_RESTRICTED);
        }

        chatRoom.setMember(member);

        chat.setChatRoom(chatRoom);
        chat.setMember(member);

        chatReport.setChat(chat);
        chatReport.setMember(member);

        chat.incrementReport();

        return chatReportRepository.save(chatReport);
    }
}

