package org.example.petwif.repository;

import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAlbum(Album album);

    // 부모 댓글만 조회하는 메서드
    List<Comment> findByAlbumAndParentCommentIsNull(Album album);
}
