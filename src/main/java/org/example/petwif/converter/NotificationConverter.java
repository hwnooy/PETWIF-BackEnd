package org.example.petwif.converter;

import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.web.dto.NotificationDTO.NotificationResponseDTO;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationConverter {

    public static NotificationResponseDTO.NotificationSettingDTO toNotificationSettingDTO(Boolean setting) {
        return NotificationResponseDTO.NotificationSettingDTO.builder()
                .setting(setting)
                .build();
    }

    public static NotificationResponseDTO.NotificationOpenDTO toNotificationOpenDTO(Notification notification) {
        Member member = notification.getRelatedMember();

        return NotificationResponseDTO.NotificationOpenDTO.builder()
                .id(notification.getId())
                .nickname(member != null ? member.getNickname() : null)
                .albumId(notification.getAlbum() != null ? notification.getAlbum().getId() : null)
                .isConfirmed(notification.getIsConfirmed())
                .build();
    }

    public static NotificationResponseDTO.NotificationResultDTO toNotificationResultDTO(Notification notification) {
        Member member = notification.getRelatedMember();

        String content = "";
        if (notification.getDtype().toString().equals("FRIEND_REQUEST")) content = member.getNickname() + "님이 친구 요청을 보냈습니다.";
        if (notification.getDtype().toString().equals("FRIEND_ACCEPT")) content = member.getNickname() + "님이 친구 요청을 수락했습니다.";
        if (notification.getDtype().toString().equals("LIKE")) content = member.getNickname() + "님이 게시글에 좋아요를 눌렀습니다.";
        if (notification.getDtype().toString().equals("BOOKMARK")) content = member.getNickname() + "님이 게시글을 북마크 했습니다.";
        if (notification.getDtype().toString().equals("COMMENT")) content = member.getNickname() + "님이 게시글에 댓글을 달았습니다.";

        return NotificationResponseDTO.NotificationResultDTO.builder()
                .id(notification.getId())
                .profile_url(member.getProfile_url())
                .nickname(member.getNickname())
                .albumId(notification.getAlbum() != null ? notification.getAlbum().getId() : null)
                .isConfirmed(notification.getIsConfirmed())
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static NotificationResponseDTO.NotificationListDTO notificationListDTO(Slice<Notification> notificationList) {

        List<NotificationResponseDTO.NotificationResultDTO> notificationDTOList = notificationList.stream()
                .map(NotificationConverter::toNotificationResultDTO).collect(Collectors.toList());

        return NotificationResponseDTO.NotificationListDTO.builder()
                .notificationList(notificationDTOList)
                .isLast(notificationList.isLast())
                .isFirst(notificationList.isFirst())
                .listSize(notificationList.getSize())
                .hasNext(notificationList.hasNext())
                .build();
    }
}
