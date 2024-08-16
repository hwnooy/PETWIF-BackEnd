package org.example.petwif.service.NotificationService;

import org.example.petwif.domain.entity.Notification;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface NotificationQueryService {

    Slice<Notification> getNotificationList(Long memberId, Integer page);
    Optional<Notification> findNotification(Long Id);
}

