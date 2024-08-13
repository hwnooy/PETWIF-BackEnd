package org.example.petwif.web.dto.NotificationDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.enums.NotificationDType;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResultDTO {
        Long id;
        Long memberId;
        Long relatedMemberId;
        String title;
        String content;
        boolean isCheck;
        NotificationDType dType;
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
