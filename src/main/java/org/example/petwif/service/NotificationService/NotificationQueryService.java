package org.example.petwif.service.NotificationService;

import org.example.petwif.domain.entity.Notification;
import org.springframework.data.domain.Slice;

public interface NotificationQueryService {

    Slice<Notification> getNotificationList(Long memberId, Integer page);
}

