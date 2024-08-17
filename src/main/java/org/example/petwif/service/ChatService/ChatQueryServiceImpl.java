package org.example.petwif.service.ChatService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Chat;
import org.example.petwif.domain.entity.ChatRoom;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.ChatRepository;
import org.example.petwif.repository.ChatRoomRepository;
import org.example.petwif.repository.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryServiceImpl implements ChatQueryService{

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    //채팅창 화면 조회
    @Override
    public Optional<Chat> findChat(Long id){
        return chatRepository.findChatById(id);
    }

    @Override
    public Slice<Chat> getChatList(Long chatRoomId, Integer page){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHATROOM_NOT_FOUND));

        Slice<Chat> ChatRoomPage = chatRepository.findAllByChatRoom(chatRoom, PageRequest.of(page, 10));

        return ChatRoomPage;
    }

    //채팅방 목록 조회
    @Override
    public Optional<ChatRoom> findChatRoom(Long id){
        return chatRoomRepository.findChatRoomById(id);
    }

    @Override
    public Slice<ChatRoom> getChatRoomList(Long memberId, Long otherId, Integer page){
        Pageable pageable = PageRequest.of(page, 10);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Member other = memberRepository.findById(member.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Slice<ChatRoom> MemberPage = chatRoomRepository.findAllByMember(member, PageRequest.of(page, 10));
        Slice<ChatRoom> OtherPage = chatRoomRepository.findAllByOther(other, PageRequest.of(page, 10));

        //member, other Slice 내용 통합 후 중복 제거
        List<ChatRoom> ChatRoomList = Stream.concat(MemberPage.getContent().stream(), OtherPage.getContent().stream()).distinct().collect(Collectors.toList());

        //Slice 생성
        boolean hasNext = ChatRoomList.size() > 10; //size가 10보다 클 경우 다음 페이지로 넘어감

        return new SliceImpl<>(ChatRoomList, pageable, hasNext);
    }

    //가장 최근 채팅 조회
    @Override
    public Optional<Chat> getLastChat(Long chatRoomId){
        return chatRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
    }
}
