package org.example.petwif.repository;

import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Notification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Slice<Notification> findAllByMemberAndIsCheckAndRelatedMemberNotInOrderByCreatedAt(Member member, boolean isCheck, List<Member> blockedMembers, PageRequest pageRequest);
}
