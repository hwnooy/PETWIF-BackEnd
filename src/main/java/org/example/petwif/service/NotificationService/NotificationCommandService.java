package org.example.petwif.service.NotificationService;

import org.example.petwif.domain.entity.Notification;

public interface NotificationCommandService {
    Notification sendNotification(Long memberId, Long relatedMemberId);
}
