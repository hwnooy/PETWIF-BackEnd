package org.example.petwif.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.domain.enums.NotificationDtype;
import org.example.petwif.repository.AlbumRepository;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.NotificationRepository;
import org.example.petwif.web.dto.NotificationDTO.NotificationRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final MemberRepository memberRepository;

    private final NotificationRepository notificationRepository;

    private final AlbumRepository albumRepository;

    @Override
    @Transactional
    public Member notificationSettingFR(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member.getFriendRequestNoti()) {
            member.setFriendRequestNoti(false);

            return memberRepository.save(member);
        }
        else {
            member.setFriendRequestNoti(true);

            return memberRepository.save(member);
        }
    }

    @Override
    @Transactional
    public Member notificationSettingFA(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member.getFriendAcceptNoti()) {
            member.setFriendAcceptNoti(false);

            return memberRepository.save(member);
        }
        else {
            member.setFriendAcceptNoti(true);

            return memberRepository.save(member);
        }
    }

    @Override
    @Transactional
    public Member notificationSettingLK(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member.getLikeNoti()) {
            member.setLikeNoti(false);

            return memberRepository.save(member);
        }
        else {
            member.setLikeNoti(true);

            return memberRepository.save(member);
        }
    }

    @Override
    @Transactional
    public Member notificationSettingBM(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member.getBookmarkNoti()) {
            member.setBookmarkNoti(false);

            return memberRepository.save(member);
        }
        else {
            member.setBookmarkNoti(true);

            return memberRepository.save(member);
        }
    }

    @Override
    @Transactional
    public Member notificationSettingCM(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member.getCommentNoti()) {
            member.setCommentNoti(false);

            return memberRepository.save(member);
        }
        else {
            member.setCommentNoti(true);

            return memberRepository.save(member);
        }
    }

    @Override
    @Transactional
    public Notification createNotification(Long relatedMemberId, Long memberId, NotificationRequestDTO.NotificationDTO request) {

        Member member = memberRepository.findById(memberId).orElse(null);

        Notification notification = new Notification();

        if (request.getDtype().equals("FRIEND_REQUEST") && !member.getFriendRequestNoti()) return null;
        if (request.getDtype().equals("FRIEND_ACCEPT") && !member.getFriendAcceptNoti()) return null;
        if (request.getDtype().equals("LIKE") && !member.getLikeNoti()) return null;
        if (request.getDtype().equals("BOOKMARK") && !member.getBookmarkNoti()) return null;
        if (request.getDtype().equals("COMMENT") && !member.getCommentNoti()) return null;

        notification.setMember(memberRepository.findById(memberId).get());
        notification.setRelatedMember(memberRepository.findById(relatedMemberId).get());
        notification.setDType(NotificationDtype.valueOf(request.getDtype()));
        notification.setAlbum(albumRepository.findById(request.getAlbumId()).orElse(null));
        notification.setIsConfirmed(false);

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public Notification openNotification(Long memberId, Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId).orElse(null);

        notification.setIsConfirmed(true);
        notificationRepository.save(notification);

        if (notification.getAlbum() == null) {
            return notification;
        }
        else {
            notification.setRelatedMember(null);
            return notification;
        }
    }
}