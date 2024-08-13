package org.example.petwif.converter;

import org.example.petwif.domain.entity.Notification;
import org.example.petwif.web.dto.NotificationDTO.NotificationResponseDTO;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationConverter {

    public static NotificationResponseDTO.NotificationResultDTO toNotificationResultDTO(Notification notification) {
        return NotificationResponseDTO.NotificationResultDTO.builder()
                .id(notification.getId())
                .memberId(notification.getMember().getId())
                .relatedMemberId(notification.getRelatedMember().getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isCheck(notification.isCheck())
                .dType(notification.getDType())
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
