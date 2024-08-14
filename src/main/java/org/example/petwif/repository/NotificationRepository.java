package org.example.petwif.repository;

import org.example.petwif.domain.entity.Notification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Slice<Notification> findByMember_IdAndIsConfirmedOrderByCreatedAt(Long memberId, Boolean isConfirmed, PageRequest pageRequest);
    Slice<Notification> findByMember_IdAndIsConfirmedAndRelatedMember_IdNotInOrderByCreatedAt(Long memberId, Boolean isConfirmed, List<Long> blockedMembers, PageRequest pageRequest);
}
