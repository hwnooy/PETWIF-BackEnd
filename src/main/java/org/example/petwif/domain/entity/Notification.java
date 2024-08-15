package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.common.BaseEntity;
import org.example.petwif.domain.enums.NotificationDtype;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="album_id", nullable = true)
    private Album album;

    private Boolean isConfirmed;

    @Enumerated(EnumType.STRING)
    private NotificationDtype dtype;

    public void setMember(Member member) {
        if (this.member != null)
            member.getNotificationList().remove(this);
        this.member = member;
        member.getNotificationList().add(this);
    }

    public void setRelatedMember(Member relatedMember) {
        this.relatedMember = relatedMember;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setDType(NotificationDtype dtype) {
        this.dtype = dtype;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
}
