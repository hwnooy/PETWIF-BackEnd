package org.example.petwif.web.dto.NotificationDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationSettingDTO {
        Boolean setting;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationOpenDTO {
        Long relatedMemberId;
        Long albumId;
        Boolean isConfirmed;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResultDTO {
        Long id;
        Long memberId;
        Long relatedMemberId;
        Long albumId;
        Boolean isConfirmed;
        String content;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationListDTO {
        List<NotificationResultDTO> notificationList;
        Integer listSize;
        Boolean isFirst;
        Boolean isLast;
        Boolean hasNext;
    }
}
