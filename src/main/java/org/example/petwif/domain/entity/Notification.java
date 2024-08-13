package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.enums.NotificationDType;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="relatedMember_id")
    private Member relatedMember;

    private String title;

    private String content;

    private boolean isCheck;

    @Enumerated(EnumType.STRING)
    private NotificationDType dType;

    public void setMember(Member member) {
        if (this.member != null)
            member.getNotificationList().remove(this);
        this.member = member;
        member.getNotificationList().add(this);
    }

    public void setRelatedMember(Member relatedMember) {
        this.relatedMember = relatedMember;
    }

    public void setDType(NotificationDType dType) {
        this.dType = dType;
    }
}
