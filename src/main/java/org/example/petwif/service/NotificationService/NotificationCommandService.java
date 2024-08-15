package org.example.petwif.service.NotificationService;

import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.web.dto.NotificationDTO.NotificationRequestDTO;

public interface NotificationCommandService {
    Member notificationSettingFR(Long memberId);
    Member notificationSettingFA(Long memberId);
    Member notificationSettingLK(Long memberId);
    Member notificationSettingBM(Long memberId);
    Member notificationSettingCM(Long memberId);
    Notification createNotification(Long relatedMemberId, Long memberId, NotificationRequestDTO.NotificationDTO request);
    Notification openNotification(Long memberId, Long notificationId);
}
