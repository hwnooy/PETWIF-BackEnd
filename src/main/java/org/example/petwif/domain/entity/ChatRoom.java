package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.enums.ChatRoomStatus;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_id")
    private Member other;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_name")
    private Member roomName;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus;

    private boolean memberStatus = false;
    private boolean otherStatus = false;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Chat> chatList = new ArrayList<>();

    //채팅방 생성
    public void setMember(Member member) {
        this.member = member;
        member.getChatRoomList().add(this);
    }

    public void setOther(Member other) {
        this.other = other;
        other.getChatRoomList().add(this);
    }

    //채팅방 나가기 (=삭제)
    public void setChatRoomStatus(ChatRoomStatus chatRoomStatus) {
        this.chatRoomStatus = chatRoomStatus;
    }

    public void setMemberStatus(boolean memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void setOtherStatus(boolean otherStatus) {
        this.otherStatus = otherStatus;
    }
}
