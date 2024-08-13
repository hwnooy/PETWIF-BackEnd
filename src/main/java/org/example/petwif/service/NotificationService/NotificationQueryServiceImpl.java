package org.example.petwif.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.handler.NotificationHandler;
import org.example.petwif.domain.entity.Block;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.repository.BlockRepository;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final MemberRepository memberRepository;

    private final NotificationRepository notificationRepository;

    private final BlockRepository blockRepository;

    @Override
    public Slice<Notification> getNotificationList(Long memberId, Integer page) {

        Member member = memberRepository.findById(memberId).get();

        List<Block> blockList = blockRepository.findByMemberId(memberId);
        List<Member> blockedMembers = blockList.stream()
                .map(Block::getTarget)
                .collect(Collectors.toList());


        Slice<Notification> notificationList = notificationRepository.findAllByMemberAndIsCheckAndRelatedMemberNotInOrderByCreatedAt(member, false, blockedMembers, PageRequest.of(page, 5));

        if (notificationList.isEmpty() || page < 0) {
            throw new NotificationHandler(ErrorStatus.NOTIFICATION_PAGE_NOT_FOUND);
        }

        return notificationList;
    }
}