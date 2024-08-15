package org.example.petwif.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.petwif.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="album_id")
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> childComments = new ArrayList<>();

    private String content;

    @Builder.Default
    private Integer likeCount = 0;  // 초기 값을 0으로 설정

    public void update(String content){
        this.content = content;
    }

    public void incrementLikeCount() {
        if (this.likeCount == null) {
            this.likeCount = 1;
        } else {
            this.likeCount += 1;
        }
    }

    public void decrementLikeCount() {
        if (this.likeCount == null || this.likeCount <= 0) {
            this.likeCount = 0;  // 음수 값이 되지 않도록 방지
        } else {
            this.likeCount -= 1;
        }
    }

    public void addChildComment(Comment comment) {
        childComments.add(comment);
        comment.setParentComment(this);
    }
}