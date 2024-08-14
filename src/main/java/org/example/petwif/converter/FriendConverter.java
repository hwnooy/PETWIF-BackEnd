package org.example.petwif.converter;

import org.example.petwif.domain.entity.Friend;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.enums.FriendStatus;
import org.example.petwif.web.dto.FriendDTO.FriendResponseDTO;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FriendConverter {

    public static FriendResponseDTO.FriendStatusDTO toFriendStatusDTO(FriendStatus friendStatus) {
        return FriendResponseDTO.FriendStatusDTO.builder()
                .status(friendStatus)
                .build();
    }

    public static FriendResponseDTO.FriendResultDTO toFriendResultDTO(Friend friend) {
        Member member = friend.getFriend();

        return FriendResponseDTO.FriendResultDTO.builder()
                .id(friend.getId())
                .memberId(friend.getMember().getId())
                .friendId(friend.getFriend().getId())
                .status(friend.getStatus())
                .profile_url(member.getProfile_url())
                .nickname(member.getNickname())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FriendResponseDTO.FriendListDTO friendListDTO(Slice<Friend> friendList) {

        List<FriendResponseDTO.FriendResultDTO> friendDTOList = friendList.stream()
                .map(FriendConverter::toFriendResultDTO).collect(Collectors.toList());

        return FriendResponseDTO.FriendListDTO.builder()
                .friendList(friendDTOList)
                .isLast(friendList.isLast())
                .isFirst(friendList.isFirst())
                .listSize(friendList.getSize())
                .hasNext(friendList.hasNext())
                .build();
    }

    public static FriendResponseDTO.RecFriendResultDTO toRecFriendResultDTO(Friend friend, Long memberId) {
        Member member = friend.getFriend();

        return FriendResponseDTO.RecFriendResultDTO.builder()
                .memberId(memberId)
                .recFriendId(friend.getFriend().getId())
                .profile_url(member.getProfile_url())
                .nickname(member.getNickname())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FriendResponseDTO.RecFriendListDTO recFriendListDTO(Slice<Friend> recFriendList, Long memberId) {

        List<FriendResponseDTO.RecFriendResultDTO> recFriendDTOList = recFriendList.stream()
                .map(friend -> toRecFriendResultDTO(friend, memberId)).collect(Collectors.toList());

        return FriendResponseDTO.RecFriendListDTO.builder()
                .recFriendList(recFriendDTOList)
                .isLast(recFriendList.isLast())
                .isFirst(recFriendList.isFirst())
                .listSize(recFriendList.getSize())
                .hasNext(recFriendList.hasNext())
                .build();
    }
}
