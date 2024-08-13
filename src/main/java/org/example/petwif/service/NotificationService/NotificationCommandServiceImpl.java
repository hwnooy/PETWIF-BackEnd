package org.example.petwif.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final MemberRepository memberRepository;

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public Notification sendNotification(Long memberId, Long relatedMemberId) {

        Notification notification = new Notification();

        notification.setMember(memberRepository.findById(memberId).get());
        notification.setRelatedMember(memberRepository.findById(relatedMemberId).get());

        return notificationRepository.save(notification);
    }
}