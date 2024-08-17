package org.example.petwif.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.handler.NotificationHandler;
import org.example.petwif.domain.entity.Block;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.repository.BlockRepository;
import org.example.petwif.repository.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    private final BlockRepository blockRepository;

    @Override
    public Slice<Notification> getNotificationList(Long memberId, Integer page) {

        List<Block> blockList = blockRepository.findByMember_Id(memberId);
        List<Long> blockedMembers = blockList.stream()
                .map(block -> block.getTarget().getId())
                .collect(Collectors.toList());

        Slice<Notification> notificationList;
        if (blockedMembers.isEmpty()) {
            notificationList = notificationRepository.findByMember_IdAndIsConfirmedOrderByCreatedAt(memberId, false, PageRequest.of(page, 5));
        } else {
            notificationList = notificationRepository.findByMember_IdAndIsConfirmedAndRelatedMember_IdNotInOrderByCreatedAt(memberId, false, blockedMembers, PageRequest.of(page, 5));
        }

        if (notificationList.isEmpty() && page != 0) {
            throw new NotificationHandler(ErrorStatus.NOTIFICATION_PAGE_NOT_FOUND);
        }

        return notificationList;
    }

    @Override
    public Optional<Notification> findNotification(Long Id) {
        return notificationRepository.findById(Id);
    }
}